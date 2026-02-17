package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.entity.InoculatedSubstrateBlockEntity;
import net.chimney.mushrooms.block.entity.ModBlockEntities;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class InoculatedSubstrateBlock extends BaseEntityBlock {
    public static final EnumProperty<InoculationStage> STAGE =
            EnumProperty.create("stage", InoculationStage.class);

    public InoculatedSubstrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STAGE, InoculationStage.INOCULATED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        InoculationStage currentStage = state.getValue(STAGE);
        if (currentStage != InoculationStage.FULLY_INOCULATED) {
            if (random.nextInt(getColonizationTickInterval()) == 0) {
                InoculationStage nextStage = currentStage.getNext();
                level.setBlock(pos, state.setValue(STAGE, nextStage), 3);

                if (canFruitAtStage(nextStage)) {
                    tryGrowMushroom(level, pos, random);
                }
            }

            if (canFruitAtStage(currentStage)) {
                tryGrowMushroom(level, pos, random);
            }
        } else {
            tryGrowMushroom(level, pos, random);
            trySpreadToMulch(level, pos, random);
        }
    }

    protected int getColonizationTickInterval() {
        return ModCommonConfig.SUBSTRATE_COLONIZATION_INTERVAL.get();
    }

    protected int getInitialMushroomGrowthStage() {
        return 0;
    }

    protected boolean canFruitAtStage(InoculationStage stage) {
        return stage == InoculationStage.FULLY_INOCULATED;
    }

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

        Block mulchBlock = ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(ChimneysMushrooms.MODID, "mulch_block")
        );
        Block dungBlock = ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(ChimneysMushrooms.MODID, "dung_block")
        );

        if ((mulchBlock != null && targetState.is(mulchBlock)) ||
                (dungBlock != null && targetState.is(dungBlock))) {
            MushroomType mushroomType = getMushroomTypeFromBlockEntity(level, pos);

            Block inoculatedMulch = ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation(ChimneysMushrooms.MODID, "inoculated_mulch")
            );

            if (inoculatedMulch != null) {
                BlockState newState = inoculatedMulch.defaultBlockState()
                        .setValue(STAGE, InoculationStage.INOCULATED);
                level.setBlockAndUpdate(targetPos, newState);

                if (level.getBlockEntity(targetPos) instanceof InoculatedSubstrateBlockEntity newBlockEntity) {
                    newBlockEntity.setMushroomType(mushroomType);
                }
            }
        }
    }

    protected void tryGrowMushroom(ServerLevel level, BlockPos pos, RandomSource random) {
        Direction growthDirection = Direction.UP;
        BlockPos growthPos = pos.relative(growthDirection);
        BlockState blockStateAtPos = level.getBlockState(growthPos);

        if (canGrowMushroomAtState(blockStateAtPos)) {
            MushroomType mushroomType = getMushroomTypeFromBlockEntity(level, pos);

            if (mushroomType != null) {
                Block mushroomBlock = getMushroomBlockForType(mushroomType, level);

                if (mushroomBlock != null) {
                    BlockState mushroomState = mushroomBlock.defaultBlockState()
                            .setValue(MushroomClusterBlock.FACING, growthDirection)
                            .setValue(MushroomClusterBlock.WATERLOGGED,
                                    blockStateAtPos.getFluidState().getType() == Fluids.WATER)
                            .setValue(MushroomClusterBlock.GROWTH_STAGE, getInitialMushroomGrowthStage());

                    level.setBlockAndUpdate(growthPos, mushroomState);
                }
            }
        }
    }

    private MushroomType getMushroomTypeFromBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof InoculatedSubstrateBlockEntity blockEntity) {
            return blockEntity.getMushroomType();
        }
        return MushroomType.BROWN;
    }


    private Block getMushroomBlockForType(MushroomType type, Level level) {
        ResourceLocation blockId;

        switch (type) {
            case BROWN:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "brown_mushroom_cluster");
                break;
            case RED:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "red_mushroom_cluster");
                break;
            case RUSSULA:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "russula_cluster");
                break;
            case BLUE_ANGEL:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "blue_angel_cluster");
                break;
            case MAGICAL_MUSHROOM:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "magical_mushroom_cluster");
                break;
            case OYSTER:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "oyster_cluster");
                break;
            case SHIITAKE:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "shiitake_cluster");
                break;
            case KING_TRUMPET:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "king_trumpet_cluster");
                break;
            case PORTOBELLO:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "portobello_cluster");
                break;
            case MOREL:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "morel_cluster");
                break;
            case FLY_AGARIC:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "fly_agaric_cluster");
                break;
            case DEATH_CAP:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "death_cap_cluster");
                break;
            case FREEDOM_CAP:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "freedom_cap_cluster");
                break;
            case CHANTERELLE:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "chanterelle_cluster");
                break;
            default:
                blockId = new ResourceLocation(ChimneysMushrooms.MODID, "brown_mushroom_cluster");
        }

        return ForgeRegistries.BLOCKS.getValue(blockId);
    }

    public static boolean canGrowMushroomAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
