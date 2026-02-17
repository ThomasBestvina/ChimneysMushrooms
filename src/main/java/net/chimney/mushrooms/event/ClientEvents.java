package net.chimney.mushrooms.event;

import com.mojang.logging.LogUtils;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation PSYCHEDELIC_SHADER =
            new ResourceLocation("chimneysmushrooms", "shaders/post/psychedelic.json");
    private static final ResourceLocation BLUE_ANGEL_SHADER =
            new ResourceLocation("chimneysmushrooms", "shaders/post/blue_angel.json");
    private static final ResourceLocation FREEDOM_CAP_SHADER =
            new ResourceLocation("chimneysmushrooms", "shaders/post/freedom_cap.json");

    private record ShaderSpec(
            String key,
            Supplier<MobEffect> effect,
            ResourceLocation postShader,
            String programName,
            float baseIntensity,
            float lerpSpeed,
            float timeScale,
            boolean usePsychedelicCurve
    ) {}

    private static final ShaderSpec PSYCHEDELIC_SPEC = new ShaderSpec(
            "psychedelic",
            () -> ModEffects.PSYCHEDELIC_EFFECT.get(),
            PSYCHEDELIC_SHADER,
            "chimneysmushrooms:psychedelic",
            0.0f,
            3.0f,
            1.0f,
            true
    );

    private static final List<ShaderSpec> SHADER_SPECS = List.of(
            PSYCHEDELIC_SPEC,
            new ShaderSpec(
                    "blue_angel",
                    () -> ModEffects.BLUE_ANGEL_EFFECT.get(),
                    BLUE_ANGEL_SHADER,
                    "chimneysmushrooms:blue_angel",
                    0.68f,
                    8.0f,
                    0.55f,
                    false
            ),
            new ShaderSpec(
                    "freedom_cap",
                    () -> ModEffects.ENLIGHTENED_EFFECT.get(),
                    FREEDOM_CAP_SHADER,
                    "chimneysmushrooms:freedom_cap",
                    0.6f,
                    8.0f,
                    1.0f,
                    false
            )
    );

    private static final Random RANDOM = new Random();
    private static final float WARMUP_DURATION = 3.0f;

    private static final Map<String, Float> intensityByKey = new HashMap<>();
    private static final Map<String, Float> seedByKey = new HashMap<>();

    private static final Set<String> activeKeys = new HashSet<>();
    private static final Set<String> previousActiveKeys = new HashSet<>();

    private static List<ShaderSpec> loadedStackSpecs = List.of();
    private static String loadedStackSignature = "";

    private static float accumulatedTime = 0.0f;
    private static long lastUpdateTime = 0L;

    private static int psychedelicStartDuration = 0;
    private static int psychedelicPreviousDuration = 0;
    private static boolean isLemonTek = false;
    private static float psychedelicWarmupTimer = 0.0f;

    private static Method addTempTargetMethod;
    private static Method getTempTargetMethod;
    private static Method addPassMethod;
    private static Method setPassMatrixMethod;
    private static Field chainProjectionField;
    private static boolean warnedAboutStackingFailure = false;
    private static boolean warnedAboutUniformFailure = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            stopAllShaders();
            return;
        }

        Map<String, MobEffectInstance> activeEffects = new HashMap<>();
        List<ShaderSpec> activeSpecs = new ArrayList<>();

        for (ShaderSpec spec : SHADER_SPECS) {
            MobEffectInstance instance = mc.player.getEffect(spec.effect().get());
            if (instance != null) {
                activeEffects.put(spec.key(), instance);
                activeSpecs.add(spec);
            }
        }

        if (activeSpecs.isEmpty()) {
            stopAllShaders();
            return;
        }

        Set<String> nextActiveKeys = activeSpecs.stream().map(ShaderSpec::key).collect(Collectors.toSet());

        for (ShaderSpec spec : activeSpecs) {
            if (!previousActiveKeys.contains(spec.key())) {
                seedByKey.put(spec.key(), RANDOM.nextFloat() * 1000.0f);
            }
            intensityByKey.putIfAbsent(spec.key(), 0.0f);
        }

        MobEffectInstance psychedelicEffect = activeEffects.get(PSYCHEDELIC_SPEC.key());
        boolean psychedelicWasActive = previousActiveKeys.contains(PSYCHEDELIC_SPEC.key());
        boolean psychedelicIsActive = psychedelicEffect != null;

        if (psychedelicIsActive && !psychedelicWasActive) {
            isLemonTek = psychedelicEffect.getAmplifier() >= 1;
            psychedelicStartDuration = psychedelicEffect.getDuration();
            psychedelicPreviousDuration = psychedelicStartDuration;
            psychedelicWarmupTimer = 0.0f;
        } else if (!psychedelicIsActive && psychedelicWasActive) {
            psychedelicStartDuration = 0;
            psychedelicPreviousDuration = 0;
            psychedelicWarmupTimer = 0.0f;
        }

        String nextSignature = buildSignature(activeSpecs);
        if (!nextSignature.equals(loadedStackSignature) || mc.gameRenderer.currentEffect() == null) {
            rebuildShaderStack(mc, activeSpecs);
        }

        activeKeys.clear();
        activeKeys.addAll(nextActiveKeys);

        previousActiveKeys.clear();
        previousActiveKeys.addAll(nextActiveKeys);
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || loadedStackSpecs.isEmpty()) {
            return;
        }

        PostChain effect = mc.gameRenderer.currentEffect();
        if (effect == null) {
            loadedStackSignature = "";
            loadedStackSpecs = List.of();
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == 0L) {
            lastUpdateTime = currentTime;
        }

        float deltaSeconds = (currentTime - lastUpdateTime) / 1000.0f;
        accumulatedTime += deltaSeconds;
        lastUpdateTime = currentTime;

        for (ShaderSpec spec : SHADER_SPECS) {
            float targetIntensity = 0.0f;
            if (activeKeys.contains(spec.key())) {
                targetIntensity = spec.usePsychedelicCurve
                        ? computePsychedelicTargetIntensity(mc, deltaSeconds)
                        : spec.baseIntensity;
            }

            float currentIntensity = intensityByKey.getOrDefault(spec.key(), 0.0f);
            intensityByKey.put(spec.key(), lerpIntensity(currentIntensity, targetIntensity, spec.lerpSpeed, deltaSeconds));
        }

        updateShaderUniforms(effect, accumulatedTime);
    }

    private static float lerpIntensity(float current, float target, float speed, float deltaSeconds) {
        float lerpSpeed = speed * deltaSeconds;
        return current + (target - current) * Math.min(1.0f, lerpSpeed);
    }

    private static float computePsychedelicTargetIntensity(Minecraft mc, float deltaSeconds) {
        MobEffectInstance effectInstance = mc.player.getEffect(ModEffects.PSYCHEDELIC_EFFECT.get());
        if (effectInstance == null) {
            return 0.0f;
        }

        int currentDuration = effectInstance.getDuration();
        if (psychedelicStartDuration == 0) {
            psychedelicStartDuration = currentDuration;
            psychedelicPreviousDuration = currentDuration;
            isLemonTek = effectInstance.getAmplifier() >= 1;
        }

        // When the effect is refreshed while still active, preserve elapsed progress
        // instead of snapping intensity back to onset.
        if (psychedelicPreviousDuration > 0 && currentDuration > psychedelicPreviousDuration) {
            psychedelicStartDuration += (currentDuration - psychedelicPreviousDuration);
        }
        psychedelicPreviousDuration = currentDuration;
        isLemonTek = isLemonTek || effectInstance.getAmplifier() >= 1;

        int elapsedTicks = psychedelicStartDuration - currentDuration;
        float elapsedSeconds = elapsedTicks / 20.0f;

        float onsetDelay = isLemonTek ? 10.0f : 25.0f;
        float rampUpTime = isLemonTek ? 25.0f : 60.0f;

        float intensity;
        if (elapsedSeconds < 0) {
            intensity = 0.0f;
        } else if (elapsedSeconds < onsetDelay) {
            intensity = 0.01f * (elapsedSeconds / onsetDelay);
        } else {
            float onsetProgress = (elapsedSeconds - onsetDelay) / rampUpTime;
            onsetProgress = Math.max(0.0f, Math.min(1.0f, onsetProgress));
            float easedProgress = onsetProgress * onsetProgress * onsetProgress;
            intensity = easedProgress * 0.95f;
        }

        if (elapsedTicks < 20) {
            float fadeIn = elapsedTicks / 20.0f;
            intensity *= fadeIn * fadeIn;
        }

        psychedelicWarmupTimer += deltaSeconds;
        if (psychedelicWarmupTimer < WARMUP_DURATION) {
            float warmupProgress = psychedelicWarmupTimer / WARMUP_DURATION;
            intensity *= warmupProgress * warmupProgress;
        }

        return intensity;
    }

    private static void rebuildShaderStack(Minecraft mc, List<ShaderSpec> activeSpecs) {
        mc.gameRenderer.shutdownEffect();

        try {
            ShaderSpec firstSpec = activeSpecs.get(0);
            mc.gameRenderer.loadEffect(firstSpec.postShader());

            PostChain effect = mc.gameRenderer.currentEffect();
            if (effect == null) {
                loadedStackSpecs = List.of();
                loadedStackSignature = "";
                return;
            }

            for (int i = 1; i < activeSpecs.size(); i++) {
                appendShaderPass(mc, effect, activeSpecs.get(i), i);
            }

            loadedStackSpecs = List.copyOf(activeSpecs);
            loadedStackSignature = buildSignature(activeSpecs);
            accumulatedTime = 0.0f;
            lastUpdateTime = 0L;
        } catch (ReflectiveOperationException | RuntimeException e) {
            if (!warnedAboutStackingFailure) {
                LOGGER.warn("Failed to build stacked post-processing shader chain; using first active shader only.", e);
                warnedAboutStackingFailure = true;
            }

            ShaderSpec firstSpec = activeSpecs.get(0);
            loadedStackSpecs = List.of(firstSpec);
            loadedStackSignature = firstSpec.key();
            accumulatedTime = 0.0f;
            lastUpdateTime = 0L;
        }
    }

    private static void appendShaderPass(Minecraft mc, PostChain effect, ShaderSpec spec, int slot) throws ReflectiveOperationException {
        initializePostChainMethods();

        String targetName = "stack_tmp_" + slot;
        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        addTempTargetMethod.invoke(effect, targetName, width, height);

        RenderTarget tempTarget = (RenderTarget) getTempTargetMethod.invoke(effect, targetName);
        RenderTarget mainTarget = mc.getMainRenderTarget();

        PostPass shaderPass = (PostPass) addPassMethod.invoke(effect, spec.programName(), mainTarget, tempTarget);
        PostPass blitPass = (PostPass) addPassMethod.invoke(effect, "blit", tempTarget, mainTarget);

        Object projection = chainProjectionField.get(effect);
        if (projection instanceof org.joml.Matrix4f matrix) {
            setPassMatrixMethod.invoke(shaderPass, matrix);
            setPassMatrixMethod.invoke(blitPass, matrix);
        }
    }

    private static void initializePostChainMethods() throws ReflectiveOperationException {
        if (addTempTargetMethod != null && getTempTargetMethod != null && addPassMethod != null
                && setPassMatrixMethod != null && chainProjectionField != null) {
            return;
        }

        addTempTargetMethod = findMethod(PostChain.class, Void.TYPE, String.class, int.class, int.class);
        getTempTargetMethod = findMethod(PostChain.class, RenderTarget.class, String.class);
        addPassMethod = findMethod(PostChain.class, PostPass.class, String.class, RenderTarget.class, RenderTarget.class);
        setPassMatrixMethod = findMethod(PostPass.class, Void.TYPE, org.joml.Matrix4f.class);
        chainProjectionField = findField(PostChain.class, org.joml.Matrix4f.class);

        addTempTargetMethod.setAccessible(true);
        getTempTargetMethod.setAccessible(true);
        addPassMethod.setAccessible(true);
        setPassMatrixMethod.setAccessible(true);
        chainProjectionField.setAccessible(true);
    }

    private static Method findMethod(Class<?> owner, Class<?> returnType, Class<?>... parameterTypes) throws NoSuchMethodException {
        for (Method method : owner.getDeclaredMethods()) {
            if (method.getReturnType() == returnType && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        throw new NoSuchMethodException("Method not found in " + owner.getName());
    }

    private static Field findField(Class<?> owner, Class<?> fieldType) throws NoSuchFieldException {
        for (Field field : owner.getDeclaredFields()) {
            if (field.getType() == fieldType) {
                return field;
            }
        }
        throw new NoSuchFieldException("Field not found in " + owner.getName());
    }

    private static void updateShaderUniforms(PostChain effect, float baseTime) {
        try {
            List<PostPass> passes = getPasses(effect);
            int shaderPassIndex = 0;

            for (PostPass pass : passes) {
                Uniform intensityUniform = pass.getEffect().getUniform("Intensity");
                if (intensityUniform == null) {
                    continue;
                }

                if (shaderPassIndex >= loadedStackSpecs.size()) {
                    break;
                }

                ShaderSpec spec = loadedStackSpecs.get(shaderPassIndex);
                shaderPassIndex++;

                Uniform timeUniform = pass.getEffect().getUniform("TripTime");
                if (timeUniform != null) {
                    timeUniform.set(baseTime * spec.timeScale());
                }

                intensityUniform.set(intensityByKey.getOrDefault(spec.key(), 0.0f));

                Uniform seedUniform = pass.getEffect().getUniform("TripSeed");
                if (seedUniform != null) {
                    seedUniform.set(seedByKey.getOrDefault(spec.key(), 0.0f));
                }

                if ("freedom_cap".equals(spec.key())) {
                    Uniform showFlagUniform = pass.getEffect().getUniform("ShowFlag");
                    if (showFlagUniform != null) {
                        showFlagUniform.set(ModCommonConfig.FREEDOM_CAP_SHOW_FLAG.get() ? 1.0f : 0.0f);
                    }
                }
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            if (!warnedAboutUniformFailure) {
                LOGGER.warn("Failed to update post-processing uniforms; clearing active effect to prevent repeated failures.", e);
                warnedAboutUniformFailure = true;
            }
            Minecraft.getInstance().gameRenderer.shutdownEffect();
            loadedStackSpecs = List.of();
            loadedStackSignature = "";
        }
    }

    @SuppressWarnings("unchecked")
    private static List<PostPass> getPasses(PostChain effect) throws IllegalAccessException {
        for (Field field : PostChain.class.getDeclaredFields()) {
            if (!List.class.isAssignableFrom(field.getType())) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(effect);
            if (!(value instanceof List<?> list)) {
                continue;
            }

            if (list.isEmpty() || list.get(0) instanceof PostPass) {
                return (List<PostPass>) list;
            }
        }

        throw new IllegalStateException("Unable to resolve PostChain pass list");
    }

    private static String buildSignature(List<ShaderSpec> specs) {
        return specs.stream().map(ShaderSpec::key).collect(Collectors.joining("|"));
    }

    private static void stopAllShaders() {
        Minecraft mc = Minecraft.getInstance();
        mc.gameRenderer.shutdownEffect();

        loadedStackSpecs = List.of();
        loadedStackSignature = "";

        activeKeys.clear();
        previousActiveKeys.clear();

        accumulatedTime = 0.0f;
        lastUpdateTime = 0L;

        psychedelicStartDuration = 0;
        psychedelicPreviousDuration = 0;
        psychedelicWarmupTimer = 0.0f;

        for (ShaderSpec spec : SHADER_SPECS) {
            intensityByKey.put(spec.key(), 0.0f);
        }
    }
}
