package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.entity.InoculatedLogBlockEntity;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InoculatedLogBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction.Axis> AXIS = RotatedPillarBlock.AXIS;
    public static final EnumProperty<LogInoculationStage> STAGE =
            EnumProperty.create("stage", LogInoculationStage.class);

    public static final BooleanProperty NORTH_MUSHROOM = BooleanProperty.create("north_mushroom");
    public static final BooleanProperty EAST_MUSHROOM = BooleanProperty.create("east_mushroom");
    public static final BooleanProperty SOUTH_MUSHROOM = BooleanProperty.create("south_mushroom");
    public static final BooleanProperty WEST_MUSHROOM = BooleanProperty.create("west_mushroom");

    private static final Direction[] HORIZONTAL_DIRECTIONS = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };

    public InoculatedLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STAGE, LogInoculationStage.INOCULATED)
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(NORTH_MUSHROOM, false)
                .setValue(EAST_MUSHROOM, false)
                .setValue(SOUTH_MUSHROOM, false)
                .setValue(WEST_MUSHROOM, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return InoculatedLogBlockEntity.getStaticType().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, AXIS, NORTH_MUSHROOM, EAST_MUSHROOM, SOUTH_MUSHROOM, WEST_MUSHROOM);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LogInoculationStage currentStage = state.getValue(STAGE);

        if (currentStage != LogInoculationStage.FULLY_COLONIZED) {
            if (random.nextInt(ModCommonConfig.LOG_COLONIZATION_CHANCE_DENOMINATOR.get()) == 0) {
                LogInoculationStage nextStage = currentStage.getNext();

                BlockState newState = state.setValue(STAGE, nextStage);

                if (nextStage == LogInoculationStage.FULLY_COLONIZED) {
                    newState = determineMushroomSides(level, pos, newState, random);
                }

                level.setBlock(pos, newState, 3);

                if (nextStage == LogInoculationStage.FULLY_COLONIZED) {
                    tryGrowMushrooms(level, pos, random);
                }
            }
        } else {
            tryGrowMushrooms(level, pos, random);
        }

        if (random.nextInt(ModCommonConfig.LOG_SPREAD_CHANCE_DENOMINATOR.get()) == 0) {
            trySpreadToConnectedLogs(level, pos, random);
        }
    }

    private BlockState determineMushroomSides(Level level, BlockPos pos, BlockState state, RandomSource random) {
        state = state.setValue(NORTH_MUSHROOM, false)
                .setValue(EAST_MUSHROOM, false)
                .setValue(SOUTH_MUSHROOM, false)
                .setValue(WEST_MUSHROOM, false);

        List<Direction> directions = new ArrayList<>();
        for (Direction direction : HORIZONTAL_DIRECTIONS) {
            BlockPos targetPos = pos.relative(direction);
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.isAir() || targetState.canBeReplaced() || targetState.getFluidState().getType() == Fluids.WATER) {
                directions.add(direction);
            }
        }

        if (directions.isEmpty()) {
            directions = new ArrayList<>(List.of(HORIZONTAL_DIRECTIONS));
        }

        Collections.shuffle(directions);

        Direction firstSide = directions.get(0);
        state = setMushroomSide(state, firstSide, true);

        if (random.nextBoolean() && directions.size() > 1) {
            Direction secondSide = directions.get(1);
            state = setMushroomSide(state, secondSide, true);
        }

        return state;
    }

    private BlockState setMushroomSide(BlockState state, Direction direction, boolean value) {
        return switch (direction) {
            case NORTH -> state.setValue(NORTH_MUSHROOM, value);
            case EAST -> state.setValue(EAST_MUSHROOM, value);
            case SOUTH -> state.setValue(SOUTH_MUSHROOM, value);
            case WEST -> state.setValue(WEST_MUSHROOM, value);
            default -> state;
        };
    }

    private boolean canGrowMushroomOnSide(BlockState state, Direction direction) {
        return switch (direction) {
            case NORTH -> state.getValue(NORTH_MUSHROOM);
            case EAST -> state.getValue(EAST_MUSHROOM);
            case SOUTH -> state.getValue(SOUTH_MUSHROOM);
            case WEST -> state.getValue(WEST_MUSHROOM);
            default -> false;
        };
    }

    private void tryGrowMushrooms(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        MushroomType mushroomType = getMushroomTypeFromBlockEntity(level, pos);
        if (mushroomType == null) return;

        for (Direction direction : HORIZONTAL_DIRECTIONS) {
            if (canGrowMushroomOnSide(state, direction)) {
                BlockPos mushroomPos = pos.relative(direction);
                BlockState currentState = level.getBlockState(mushroomPos);

                if (currentState.isAir() || currentState.getFluidState().getType() == Fluids.WATER) {
                    if (random.nextInt(ModCommonConfig.LOG_MUSHROOM_GROWTH_CHANCE_DENOMINATOR.get()) == 0) {
                        Block mushroomBlock = getMushroomBlockForType(mushroomType);
                        if (mushroomBlock != null) {
                            BlockState mushroomState = mushroomBlock.defaultBlockState()
                                    .setValue(LogMushroomClusterBlock.FACING, direction)
                                    .setValue(LogMushroomClusterBlock.WATERLOGGED,
                                            currentState.getFluidState().getType() == Fluids.WATER)
                                    .setValue(LogMushroomClusterBlock.GROWTH_STAGE, 0);

                            level.setBlockAndUpdate(mushroomPos, mushroomState);
                        }
                    }
                }
            }
        }
    }

    private void trySpreadToConnectedLogs(ServerLevel level, BlockPos pos, RandomSource random) {
        MushroomType mushroomType = getMushroomTypeFromBlockEntity(level, pos);
        if (mushroomType == null) return;

        List<BlockPos> validLogPositions = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            BlockState adjacentState = level.getBlockState(adjacentPos);

            if (isValidLogToInoculate(adjacentState.getBlock()) &&
                    !(adjacentState.getBlock() instanceof InoculatedLogBlock)) {
                validLogPositions.add(adjacentPos);
            }

            if (adjacentState.getBlock() instanceof InoculatedLogBlock) {
                BlockPos secondPos = adjacentPos.relative(direction);
                BlockState secondState = level.getBlockState(secondPos);

                if (isValidLogToInoculate(secondState.getBlock()) &&
                        !(secondState.getBlock() instanceof InoculatedLogBlock)) {
                    validLogPositions.add(secondPos);
                }
            }
        }

        if (!validLogPositions.isEmpty()) {
            BlockPos targetPos = validLogPositions.get(random.nextInt(validLogPositions.size()));
            BlockState targetState = level.getBlockState(targetPos);

            Direction.Axis axis = level.getBlockState(pos).getValue(AXIS);
            if (targetState.hasProperty(RotatedPillarBlock.AXIS)) {
                axis = targetState.getValue(RotatedPillarBlock.AXIS);
            }

            BlockState inoculatedLog = ModBlocks.INOCULATED_LOG.get().defaultBlockState()
                    .setValue(AXIS, axis)
                    .setValue(STAGE, LogInoculationStage.INOCULATED)
                    .setValue(NORTH_MUSHROOM, false)
                    .setValue(EAST_MUSHROOM, false)
                    .setValue(SOUTH_MUSHROOM, false)
                    .setValue(WEST_MUSHROOM, false);

            level.setBlock(targetPos, inoculatedLog, 3);

            if (level.getBlockEntity(targetPos) instanceof InoculatedLogBlockEntity blockEntity) {
                blockEntity.setOriginalLogState(targetState);
                blockEntity.setMushroomType(mushroomType);
            }
        }
    }

    private boolean isValidLogToInoculate(Block block) {
        return MushroomType.isWoodBlock(block);
    }

    private MushroomType getMushroomTypeFromBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof InoculatedLogBlockEntity blockEntity) {
            return blockEntity.getMushroomType();
        }
        return MushroomType.LIONS_MANE;
    }

    private Block getMushroomBlockForType(MushroomType type) {
        return switch (type) {
            case LIONS_MANE -> ModBlocks.LIONS_MANE_CLUSTER.get();
            case CHANTERELLE -> ModBlocks.CHANTERELLE_CLUSTER.get();
            case OYSTER -> ModBlocks.OYSTER_CLUSTER.get();
            case SHIITAKE -> ModBlocks.SHIITAKE_CLUSTER.get();
            case KING_TRUMPET -> ModBlocks.KING_TRUMPET_CLUSTER.get();
            default -> null;
        };
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP) {
            return ModBlocks.STRIPPED_MYCELIATED_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

}
