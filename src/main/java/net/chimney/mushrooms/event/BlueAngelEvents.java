package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.effect.ModEffects;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlueAngelEvents {
    private static final Set<Integer> ADJUSTING_DURATION = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(ModEffects.BLUE_ANGEL_EFFECT.get())) {
            return;
        }

        MobEffectInstance savedEffect = entity.getEffect(ModEffects.BLUE_ANGEL_EFFECT.get());
        int remainingDuration = savedEffect != null
                ? savedEffect.getDuration()
                : ModCommonConfig.BLUE_ANGEL_EFFECT_DURATION_TICKS.get();

        event.setCanceled(true);

        entity.setHealth((float) ModCommonConfig.BLUE_ANGEL_SURVIVAL_HEALTH.get().doubleValue());

        entity.removeAllEffects();

        entity.addEffect(new MobEffectInstance(
                ModEffects.BLUE_ANGEL_EFFECT.get(),
                remainingDuration,
                0,
                false,
                false,
                true
        ));

        entity.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                ModCommonConfig.BLUE_ANGEL_TOTEM_REGEN_DURATION_TICKS.get(),
                ModCommonConfig.BLUE_ANGEL_TOTEM_REGEN_AMPLIFIER.get()
        ));
        entity.addEffect(new MobEffectInstance(
                MobEffects.ABSORPTION,
                ModCommonConfig.BLUE_ANGEL_TOTEM_ABSORPTION_DURATION_TICKS.get(),
                ModCommonConfig.BLUE_ANGEL_TOTEM_ABSORPTION_AMPLIFIER.get()
        ));
        entity.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE,
                ModCommonConfig.BLUE_ANGEL_TOTEM_FIRE_RES_DURATION_TICKS.get(),
                ModCommonConfig.BLUE_ANGEL_TOTEM_FIRE_RES_AMPLIFIER.get()
        ));

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                net.minecraft.sounds.SoundEvents.TOTEM_USE,
                entity.getSoundSource(), 1.0F, 1.0F);

        spawnTotemParticles(entity);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(ModEffects.BLUE_ANGEL_EFFECT.get())) {
            return;
        }

        event.setCanceled(true);

        if (entity.getHealth() - event.getAmount() <= 0) {
            entity.setHealth(entity.getMaxHealth());

            entity.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    ModCommonConfig.BLUE_ANGEL_TOTEM_REGEN_DURATION_TICKS.get(),
                    ModCommonConfig.BLUE_ANGEL_TOTEM_REGEN_AMPLIFIER.get()
            ));

            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    net.minecraft.sounds.SoundEvents.TOTEM_USE,
                    entity.getSoundSource(), 1.0F, 1.0F);

            spawnTotemParticles(entity);
        }
    }

    @SubscribeEvent
    public static void onBlueAngelConsumed(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().getItem() != ModItems.BLUE_ANGEL.get()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        entity.removeEffect(MobEffects.REGENERATION);
        entity.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                ModCommonConfig.BLUE_ANGEL_ITEM_REGEN_DURATION_TICKS.get(),
                ModCommonConfig.BLUE_ANGEL_ITEM_REGEN_AMPLIFIER.get()
        ));
    }

    @SubscribeEvent
    public static void onEffectAdded(net.minecraftforge.event.entity.living.MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == ModEffects.BLUE_ANGEL_EFFECT.get()) {
            LivingEntity entity = event.getEntity();
            int entityId = entity.getId();

            if (!entity.level().isClientSide() && ADJUSTING_DURATION.add(entityId)) {
                try {
                    MobEffectInstance current = event.getEffectInstance();
                    int configuredDuration = ModCommonConfig.BLUE_ANGEL_EFFECT_DURATION_TICKS.get();
                    if (current.getDuration() != configuredDuration) {
                        entity.removeEffect(ModEffects.BLUE_ANGEL_EFFECT.get());
                        entity.addEffect(new MobEffectInstance(
                                ModEffects.BLUE_ANGEL_EFFECT.get(),
                                configuredDuration,
                                current.getAmplifier(),
                                current.isAmbient(),
                                current.isVisible(),
                                current.showIcon()
                        ));
                    }
                } finally {
                    ADJUSTING_DURATION.remove(entityId);
                }
            }

            if (!entity.level().isClientSide()) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE,
                        entity.getSoundSource(), 1.0F, 1.0F);

                spawnActivationParticles(entity);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(net.minecraftforge.event.entity.living.MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() == ModEffects.BLUE_ANGEL_EFFECT.get()) {
            LivingEntity entity = event.getEntity();

            if (!entity.level().isClientSide()) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE,
                        entity.getSoundSource(), 1.0F, 1.0F);

                spawnDeactivationParticles(entity);
            }
        }
    }

    private static void spawnTotemParticles(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entity.level();

            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.TOTEM_OF_UNDYING,
                    entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                    100, 0.5, 0.5, 0.5, 0.3
            );
        }
    }

    private static void spawnImmuneParticles(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entity.level();

            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = entity.getX() + Math.cos(angle) * entity.getBbWidth();
                double z = entity.getZ() + Math.sin(angle) * entity.getBbWidth();
                double y = entity.getY() + entity.getBbHeight() * 0.5 + Math.sin(angle * 2) * 0.2;

                serverLevel.sendParticles(
                        net.minecraft.core.particles.ParticleTypes.ENCHANT,
                        x, y, z,
                        2, 0.1, 0.1, 0.1, 0.05
                );
            }
        }
    }

    private static void spawnActivationParticles(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entity.level();

            for (int ring = 1; ring <= 3; ring++) {
                for (int i = 0; i < 360; i += 10) {
                    double angle = Math.toRadians(i);
                    double radius = ring * 1.5;
                    double x = entity.getX() + Math.cos(angle) * radius;
                    double z = entity.getZ() + Math.sin(angle) * radius;
                    double y = entity.getY() + entity.getBbHeight() * 0.5;

                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.ENCHANT,
                            x, y, z,
                            3, 0.2, 0.5, 0.2, 0.1
                    );
                }
            }
        }
    }

    private static void spawnDeactivationParticles(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entity.level();

            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ(),
                    50, 0.5, 0.2, 0.5, 0.05
            );
        }
    }
}
