package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.entity.InoculatedSubstrateBlockEntity;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InoculatedMulchBlock extends InoculatedSubstrateBlock {

    public InoculatedMulchBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return InoculatedSubstrateBlockEntity.getStaticType().create(pos, state);
        } catch (IllegalStateException e) {
            ChimneysMushrooms.LOGGER.error("Failed to create block entity: {}", e.getMessage());
            return new InoculatedSubstrateBlockEntity(pos, state);
        }
    }

    public static boolean canMushroomGrowOnDung(MushroomType type) {
        if (type == MushroomType.BLUE_ANGEL || type.isLogBased()) {
            return false;
        }

        return type.canPlantOn(Blocks.DIRT)
                || type.canPlantOn(ModBlocks.DUNG_BLOCK.get())
                || type.canPlantOn(ModBlocks.MULCH_BLOCK.get());
    }

    @Override
    protected int getColonizationTickInterval() {
        int substrateInterval = ModCommonConfig.SUBSTRATE_COLONIZATION_INTERVAL.get();
        int configuredMulchInterval = ModCommonConfig.MULCH_COLONIZATION_INTERVAL.get();
        // Guarantee mulch colonizes at least 2x faster than substrate, even if old configs are equal.
        return Math.max(1, Math.min(configuredMulchInterval, substrateInterval / 2));
    }

    @Override
    protected int getInitialMushroomGrowthStage() {
        return 2;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (level.getBlockEntity(pos) instanceof InoculatedSubstrateBlockEntity blockEntity) {
            MushroomType type = blockEntity.getMushroomType();
            if (!canMushroomGrowOnDung(type)) {
                ChimneysMushrooms.LOGGER.warn(
                        "Invalid mushroom type {} for dung block at {}. Defaulting to BROWN.",
                        type.getName(), pos
                );
                blockEntity.setMushroomType(MushroomType.BROWN);
            }
        }
    }
}
