package net.chimney.mushrooms.worldgen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, ChimneysMushrooms.MODID);

    public static final RegistryObject<Feature<SimpleBlockConfiguration>> LOG_MUSHROOM =
            FEATURES.register("log_mushroom", () -> new LogMushroomFeature(SimpleBlockConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
