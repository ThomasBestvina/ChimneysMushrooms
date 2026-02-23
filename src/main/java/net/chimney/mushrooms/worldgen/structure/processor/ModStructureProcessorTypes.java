package net.chimney.mushrooms.worldgen.structure.processor;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, ChimneysMushrooms.MODID);

    public static final RegistryObject<StructureProcessorType<MycologistHouseProcessor>> MYCOLOGIST_HOUSE_PROCESSOR =
            PROCESSOR_TYPES.register("mycologist_house_processor", () -> () -> MycologistHouseProcessor.CODEC);

    public static void register(IEventBus eventBus) {
        PROCESSOR_TYPES.register(eventBus);
    }
}
