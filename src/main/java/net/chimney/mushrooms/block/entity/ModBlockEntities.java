package net.chimney.mushrooms.block.entity;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChimneysMushrooms.MODID);

    private static RegistryObject<BlockEntityType<InoculatedSubstrateBlockEntity>> INOCULATED_SUBSTRATE_HOLDER;

    public static final RegistryObject<BlockEntityType<InoculatedLogBlockEntity>> INOCULATED_LOG_HOLDER =
            BLOCK_ENTITIES.register("inoculated_log",
                    () -> BlockEntityType.Builder.of(
                            InoculatedLogBlockEntity::new,
                            ModBlocks.INOCULATED_LOG.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {

        BLOCK_ENTITIES.register(eventBus);

        INOCULATED_SUBSTRATE_HOLDER = BLOCK_ENTITIES.register("inoculated_substrate",
                () -> BlockEntityType.Builder.of(
                        InoculatedSubstrateBlockEntity::new,
                        ModBlocks.INOCULATED_SUBSTRATE.get()
                ).build(null));


    }

    public static BlockEntityType<InoculatedSubstrateBlockEntity> getInoculatedSubstrateType() {
        if (INOCULATED_SUBSTRATE_HOLDER == null) {
            throw new IllegalStateException("BlockEntities not registered yet!");
        }
        return INOCULATED_SUBSTRATE_HOLDER.get();
    }
    public static BlockEntityType<InoculatedLogBlockEntity> getInoculatedLogType() {
        return INOCULATED_LOG_HOLDER.get();
    }

    public static void init() {

        InoculatedSubstrateBlockEntity.setType(getInoculatedSubstrateType());
        InoculatedLogBlockEntity.setType(getInoculatedLogType()); // Add this
    }
}
