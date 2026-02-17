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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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

public class MushroomClusterBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 3);

    protected final VoxelShape[] shapes = new VoxelShape[4];

    public MushroomClusterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.UP)
                .setValue(GROWTH_STAGE, 0));

        this.shapes[0] = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
        this.shapes[1] = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D);
        this.shapes[2] = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
        this.shapes[3] = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        VoxelShape shape = shapes[state.getValue(GROWTH_STAGE)];

        return rotateShape(shape, direction);
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

    private VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        return shape;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int currentStage = state.getValue(GROWTH_STAGE);

        if (currentStage < 3 && random.nextInt(ModCommonConfig.CLUSTER_GROWTH_CHANCE_DENOMINATOR.get()) == 0) {
            level.setBlock(pos, state.setValue(GROWTH_STAGE, currentStage + 1), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GROWTH_STAGE) < 3;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(direction.getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);

        if (MushroomType.isAnyWoodBlock(attachedState.getBlock())) {
            return attachedState.isFaceSturdy(level, attachedPos, direction);
        }

        if (direction == Direction.DOWN || direction == Direction.UP) {
            return (attachedState.getBlock() instanceof InoculatedSubstrateBlock &&
                    attachedState.getValue(InoculatedSubstrateBlock.STAGE) == InoculationStage.FULLY_INOCULATED) ||
                    attachedState.isFaceSturdy(level, attachedPos, direction);
        }
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
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
