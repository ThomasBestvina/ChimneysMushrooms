package net.chimney.mushrooms.potion;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ChimneysMushrooms.MODID);

    public static final RegistryObject<Potion> PSYCHEDELIC_POTION = POTIONS.register("psychedelic_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.PSYCHEDELIC_EFFECT.getHolder().get().get(), 18000,1)));

    public static final RegistryObject<Potion> DIARRHEA_POTION = POTIONS.register("diarrhea_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.DUNG_TRAIL_EFFECT.getHolder().get().get(), 1200, 0),
                    new MobEffectInstance(MobEffects.CONFUSION, 600, 0)));

    public static final RegistryObject<Potion> WITHERING_POTION = POTIONS.register("withering_potion",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 1200, 1)));

    public static void register(IEventBus eventBus)
    {
        POTIONS.register(eventBus);
    }
}
