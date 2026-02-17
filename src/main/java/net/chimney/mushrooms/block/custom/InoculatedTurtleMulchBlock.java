package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.entity.InoculatedSubstrateBlockEntity;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class InoculatedTurtleMulchBlock extends InoculatedSubstrateBlock {

    public InoculatedTurtleMulchBlock(Properties properties) {
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

    public static boolean canMushroomGrowOnTurtleMulch(MushroomType type) {
        return type == MushroomType.BLUE_ANGEL;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (level.getBlockEntity(pos) instanceof InoculatedSubstrateBlockEntity blockEntity) {
            MushroomType type = blockEntity.getMushroomType();
            if (!canMushroomGrowOnTurtleMulch(type)) {
                ChimneysMushrooms.LOGGER.warn(
                        "Invalid mushroom type {} for turtle mulch block at {}. Defaulting to BLUE_ANGEL.",
                        type.getName(), pos
                );
                blockEntity.setMushroomType(MushroomType.BLUE_ANGEL);
            }
        }
    }

    @Override
    protected void trySpreadToMulch(ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(ModCommonConfig.SUBSTRATE_SPREAD_CHANCE_DENOMINATOR.get()) != 0) {
            return;
        }

        Direction[] horizontalDirections = new Direction[]{
                Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };

        Direction spreadDirection = horizontalDirections[random.nextInt(horizontalDirections.length)];
        BlockPos targetPos = pos.relative(spreadDirection);
        BlockState targetState = level.getBlockState(targetPos);

        Block turtleMulchBlock = ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(ChimneysMushrooms.MODID, "turtle_mulch_block")
        );

        if (turtleMulchBlock != null && targetState.is(turtleMulchBlock)) {
            MushroomType mushroomType = MushroomType.BLUE_ANGEL; // Turtle mulch only has blue angel

            Block inoculatedTurtleMulch = ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation(ChimneysMushrooms.MODID, "inoculated_turtle_mulch")
            );

            if (inoculatedTurtleMulch != null) {
                BlockState newState = inoculatedTurtleMulch.defaultBlockState()
                        .setValue(STAGE, InoculationStage.INOCULATED);
                level.setBlockAndUpdate(targetPos, newState);

                if (level.getBlockEntity(targetPos) instanceof InoculatedSubstrateBlockEntity newBlockEntity) {
                    newBlockEntity.setMushroomType(mushroomType);
                }
            }
        }
    }
}
