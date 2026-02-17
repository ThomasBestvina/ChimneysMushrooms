package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.mushroom.MushroomType;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LogMushroomClusterBlock extends Block {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 3);

    protected final VoxelShape[][] shapesByFacing = new VoxelShape[Direction.values().length][4];

    public LogMushroomClusterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH) // Default to horizontal
                .setValue(GROWTH_STAGE, 0));

        this.shapesByFacing[Direction.NORTH.ordinal()] = new VoxelShape[]{
                Block.box(2.0D, 4.0D, 10.0D, 14.0D, 11.0D, 16.0D),
                Block.box(1.0D, 4.0D, 8.0D, 15.0D, 11.0D, 16.0D),
                Block.box(0.0D, 4.0D, 6.0D, 16.0D, 11.0D, 16.0D),
                Block.box(0.0D, 4.0D, 4.0D, 16.0D, 11.0D, 16.0D)
        };
        this.shapesByFacing[Direction.SOUTH.ordinal()] = new VoxelShape[]{
                Block.box(2.0D, 4.0D, 0.0D, 14.0D, 11.0D, 6.0D),
                Block.box(1.0D, 4.0D, 0.0D, 15.0D, 11.0D, 8.0D),
                Block.box(0.0D, 4.0D, 0.0D, 16.0D, 11.0D, 10.0D),
                Block.box(0.0D, 4.0D, 0.0D, 16.0D, 11.0D, 12.0D)
        };
        this.shapesByFacing[Direction.EAST.ordinal()] = new VoxelShape[]{
                Block.box(0.0D, 4.0D, 2.0D, 6.0D, 11.0D, 14.0D),
                Block.box(0.0D, 4.0D, 1.0D, 8.0D, 11.0D, 15.0D),
                Block.box(0.0D, 4.0D, 0.0D, 10.0D, 11.0D, 16.0D),
                Block.box(0.0D, 4.0D, 0.0D, 12.0D, 11.0D, 16.0D)
        };
        this.shapesByFacing[Direction.WEST.ordinal()] = new VoxelShape[]{
                Block.box(10.0D, 4.0D, 2.0D, 16.0D, 11.0D, 14.0D),
                Block.box(8.0D, 4.0D, 1.0D, 16.0D, 11.0D, 15.0D),
                Block.box(6.0D, 4.0D, 0.0D, 16.0D, 11.0D, 16.0D),
                Block.box(4.0D, 4.0D, 0.0D, 16.0D, 11.0D, 16.0D)
        };
        this.shapesByFacing[Direction.UP.ordinal()] = new VoxelShape[]{
                Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
        };
        this.shapesByFacing[Direction.DOWN.ordinal()] = new VoxelShape[]{
                Block.box(1.0D, 13.0D, 1.0D, 15.0D, 16.0D, 15.0D),
                Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D)
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return shapesByFacing[direction.ordinal()][state.getValue(GROWTH_STAGE)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(GROWTH_STAGE, 3)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int currentStage = state.getValue(GROWTH_STAGE);

        if (currentStage < 3) {
            if (canSurvive(state, level, pos)) {
                if (random.nextInt(ModCommonConfig.CLUSTER_GROWTH_CHANCE_DENOMINATOR.get()) == 0) {
                    level.setBlock(pos, state.setValue(GROWTH_STAGE, currentStage + 1), 3);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(direction.getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);

        if (MushroomType.isAnyWoodBlock(attachedState.getBlock())) {
            return attachedState.isFaceSturdy(level, attachedPos, direction);
        }

        if (direction == Direction.UP) {
            return attachedState.isFaceSturdy(level, attachedPos, Direction.UP);
        }

        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, GROWTH_STAGE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
