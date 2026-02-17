package net.chimney.mushrooms.effect;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;
import java.util.jar.Attributes;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChimneysMushrooms.MODID);

    public static final RegistryObject<MobEffect> PSYCHEDELIC_EFFECT = MOB_EFFECTS.register("psychedelic",
            () -> new PsychedelicEffect());

    public static final RegistryObject<MobEffect> DUNG_TRAIL_EFFECT = MOB_EFFECTS.register("dung_trail",
            () -> new DungTrailEffect());

    public static final RegistryObject<MobEffect> BLUE_ANGEL_EFFECT = MOB_EFFECTS.register("blue_angel",
            () -> new BlueAngelEffect());

    public static final RegistryObject<MobEffect> ENLIGHTENED_EFFECT = MOB_EFFECTS.register("enlightened",
            () -> new EnlightenedEffect());

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
