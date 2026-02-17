package net.chimney.mushrooms.item.custom;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.custom.InoculatedLogBlock;
import net.chimney.mushrooms.block.custom.InoculatedMulchBlock;
import net.chimney.mushrooms.block.custom.LogInoculationStage;
import net.chimney.mushrooms.block.entity.InoculatedLogBlockEntity;
import net.chimney.mushrooms.block.entity.InoculatedSubstrateBlockEntity;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnBagItem extends Item {
    private static final String MUSHROOM_TYPE_KEY = "MushroomType";
    private static final String START_TIME_KEY = "StartTime";
    private static final String GROWTH_PERCENT_KEY = "GrowthPercent";
    private static final String READY_KEY = "Ready";

    public SpawnBagItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(net.minecraft.world.item.Rarity.COMMON));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide || !(entity instanceof Player player)) {
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();

        if (tag.getBoolean(READY_KEY)) {
            return;
        }

        if (!tag.contains(MUSHROOM_TYPE_KEY)) {
            tag.putString(MUSHROOM_TYPE_KEY, MushroomType.BROWN.name());
            tag.putLong(START_TIME_KEY, level.getGameTime());
            return;
        }

        MushroomType type = getMushroomTypeFromTag(tag);
        long startTime = tag.getLong(START_TIME_KEY);
        long currentTime = level.getGameTime();
        long elapsedTicks = currentTime - startTime;

        int configuredGrowthTime = ModCommonConfig.getSpawnBagGrowthTime(type, type.getGrowthTime());
        float baseGrowth = Math.min(1.0f, (float)elapsedTicks / configuredGrowthTime);
        float growthModifier = type.getGrowthModifier(level, player.blockPosition());
        float growthPercent = Math.min(1.0f, baseGrowth * growthModifier);

        float oldGrowth = tag.getFloat(GROWTH_PERCENT_KEY);

        if (currentTime % 20 == 0 || growthPercent >= 1.0f || Math.floor(oldGrowth * 13) != Math.floor(growthPercent * 13)) {
            tag.putFloat(GROWTH_PERCENT_KEY, growthPercent);
        }

        if (growthPercent >= 1.0f && !tag.getBoolean(READY_KEY)) {
            tag.putBoolean(READY_KEY, true);
            onBecomeReady(stack, player, type);
        }
    }

    private void onBecomeReady(ItemStack stack, Player player, MushroomType type) {
        player.level().playSound(null, player.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                0.5f, 1.0f);

        stack.setHoverName(getReadyName(type));

        player.displayClientMessage(
                Component.translatable("message.chimneysmushrooms.bag_ready")
                        .withStyle(ChatFormatting.GREEN),
                true);
    }

    private Component getReadyName(MushroomType type) {
        return Component.translatable("item.chimneysmushrooms.ready_spawn_bag")
                .append(" (")
                .append(Component.translatable("mushroom.chimneysmushrooms." + type.getName()))
                .append(")")
                .withStyle(ChatFormatting.GREEN);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        Direction direction = context.getClickedFace();

        if (player == null) return InteractionResult.PASS;

        CompoundTag tag = stack.getOrCreateTag();
        MushroomType type = getMushroomType(stack);

        if (!tag.getBoolean(READY_KEY)) {
            if (level.isClientSide) {
                float growth = getGrowthPercent(stack);
                int percent = (int)(growth * 100);
                player.displayClientMessage(
                        Component.translatable("message.chimneysmushrooms.bag_not_ready", percent)
                                .withStyle(ChatFormatting.YELLOW),
                        true);
            }
            return InteractionResult.FAIL;
        }

        BlockState clickedState = level.getBlockState(pos);
        if (type.isLogBased()) {
            return plantOnLog(context, type, stack, player);
        }

        if (canConvertToSubstrate(clickedState.getBlock())) {
            if (!level.isClientSide) {
                convertToSubstrate(type, level, pos, stack, player);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (type.canPlantOn(clickedState.getBlock())) {
            BlockPos plantPos = pos.relative(direction);
            if (canPlaceAt(level, plantPos)) {
                if (!level.isClientSide) {
                    plantMushroom(type, level, plantPos, stack, player);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    private boolean canConvertToSubstrate(Block block) {
        return block == Blocks.DIRT ||
                block == Blocks.GRASS_BLOCK ||
                block == Blocks.MYCELIUM ||
                block == Blocks.CRIMSON_NYLIUM ||
                block == Blocks.WARPED_NYLIUM ||
                block == Blocks.PODZOL ||
                block == ModBlocks.MULCH_BLOCK.get() ||
                block == ModBlocks.TURTLE_MULCH_BLOCK.get();
    }

    private InteractionResult plantOnLog(UseOnContext context, MushroomType type,
                                         ItemStack stack, Player player) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState clickedState = level.getBlockState(pos);

        if (clickedState.getBlock() instanceof InoculatedLogBlock) {
            return InteractionResult.PASS;
        }

        if (isValidLog(clickedState)) {
            if (!level.isClientSide) {
                Direction.Axis axis = Direction.Axis.Y;
                if (clickedState.hasProperty(RotatedPillarBlock.AXIS)) {
                    axis = clickedState.getValue(RotatedPillarBlock.AXIS);
                }

                BlockState inoculatedLog = ModBlocks.INOCULATED_LOG.get().defaultBlockState()
                        .setValue(InoculatedLogBlock.AXIS, axis)
                        .setValue(InoculatedLogBlock.STAGE, LogInoculationStage.INOCULATED);

                level.setBlock(pos, inoculatedLog, 3);

                if (level.getBlockEntity(pos) instanceof InoculatedLogBlockEntity blockEntity) {
                    blockEntity.setOriginalLogState(clickedState);
                    blockEntity.setMushroomType(type);
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                level.playSound(null, pos, type.getPlantingSound(), SoundSource.BLOCKS, 1.0f, 0.9f);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(type.getPlantingParticle(),
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            12, 0.3, 0.3, 0.3, 0.05);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private boolean isValidLog(BlockState state) {
        return MushroomType.isAnyWoodBlock(state.getBlock());
    }

    private void convertToSubstrate(MushroomType type, Level level, BlockPos pos, ItemStack stack, Player player) {
        Block currentBlock = level.getBlockState(pos).getBlock();

        if (type == MushroomType.BLUE_ANGEL && currentBlock != ModBlocks.TURTLE_MULCH_BLOCK.get()) {
            player.displayClientMessage(
                    Component.translatable("message.chimneysmushrooms.cannot_grow_on_turtle_mulch")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return;
        }

        BlockState substrateState;
        if (currentBlock == ModBlocks.TURTLE_MULCH_BLOCK.get()) {
            if (!net.chimney.mushrooms.block.custom.InoculatedTurtleMulchBlock.canMushroomGrowOnTurtleMulch(type)) {
                player.displayClientMessage(
                        Component.translatable("message.chimneysmushrooms.cannot_grow_on_turtle_mulch")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                return;
            }
            substrateState = ModBlocks.INOCULATED_TURTLE_MULCH.get().defaultBlockState();
        } else if (currentBlock == ModBlocks.MULCH_BLOCK.get()) {
            if (!InoculatedMulchBlock.canMushroomGrowOnDung(type)) {
                player.displayClientMessage(
                        Component.translatable("message.chimneysmushrooms.cannot_grow_on_dung")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                return;
            }
            substrateState = ModBlocks.INOCULATED_MULCH.get().defaultBlockState();
        } else {
            substrateState = ModBlocks.INOCULATED_SUBSTRATE.get().defaultBlockState();
        }

        level.setBlock(pos, substrateState, 3);

        if (level.getBlockEntity(pos) instanceof InoculatedSubstrateBlockEntity blockEntity) {
            blockEntity.setMushroomType(type);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        level.playSound(null, pos, type.getPlantingSound(), SoundSource.BLOCKS, 1.0f, 1.0f);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(type.getPlantingParticle(),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    8, 0.3, 0.3, 0.3, 0.05);
        }
    }

    private boolean canPlaceAt(Level level, BlockPos pos) {
        return level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced();
    }

    private void plantMushroom(MushroomType type, Level level, BlockPos pos, ItemStack stack, Player player) {
        BlockState substrateState = ModBlocks.INOCULATED_SUBSTRATE.get().defaultBlockState();

        if (type.canSurviveAt(level, pos, substrateState)) {
            level.setBlock(pos, substrateState, 3);

            if (level.getBlockEntity(pos) instanceof InoculatedSubstrateBlockEntity blockEntity) {
                blockEntity.setMushroomType(type);
                blockEntity.setChanged(); // Mark as dirty to save
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            level.playSound(null, pos, type.getPlantingSound(), SoundSource.BLOCKS, 1.0f, 1.0f);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(type.getPlantingParticle(),
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        8, 0.3, 0.3, 0.3, 0.05);
            }
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                showBagStatus(stack, player);
            }
            return InteractionResultHolder.success(stack);
        }

        return super.use(level, player, hand);
    }

    private void showBagStatus(ItemStack stack, Player player) {
        MushroomType type = getMushroomType(stack);
        boolean ready = isReady(stack);

        if (ready) {
            player.displayClientMessage(
                    Component.translatable("message.chimneysmushrooms.bag_status_ready")
                            .append(" (")
                            .append(Component.translatable("mushroom.chimneysmushrooms." + type.getName()))
                            .append(")")
                            .withStyle(ChatFormatting.GREEN),
                    true);
        } else {
            float growth = getGrowthPercent(stack);
            int percent = (int)(growth * 100);
            player.displayClientMessage(
                    Component.translatable("message.chimneysmushrooms.bag_status_growing",
                                    Component.translatable("mushroom.chimneysmushrooms." + type.getName()),
                                    percent)
                            .withStyle(ChatFormatting.YELLOW),
                    true);
        }
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !isReady(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0f * getGrowthPercent(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        MushroomType type = getMushroomType(stack);
        float growth = getGrowthPercent(stack);

        float r = type.getRed() + (1.0f - type.getRed()) * growth;
        float g = type.getGreen() + (1.0f - type.getGreen()) * growth;
        float b = type.getBlue() + (1.0f - type.getBlue()) * growth;

        int ir = (int)(r * 255);
        int ig = (int)(g * 255);
        int ib = (int)(b * 255);

        return (ir << 16) | (ig << 8) | ib;
    }

    @Override
    public Component getName(ItemStack stack) {
        MushroomType type = getMushroomType(stack);

        if (isReady(stack)) {
            return getReadyName(type);
        }

        float growth = getGrowthPercent(stack);
        String stageKey = getGrowthStage(growth);

        return Component.translatable("item.chimneysmushrooms.spawn_bag." + stageKey)
                .append(" (")
                .append(Component.translatable("mushroom.chimneysmushrooms." + type.getName()))
                .append(")");
    }

    private String getGrowthStage(float growth) {
        if (growth < 0.25f) return "fresh";
        if (growth < 0.5f) return "colonizing";
        if (growth < 0.75f) return "mostly_colonized";
        return "nearly_ready";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        MushroomType type = getMushroomType(stack);
        boolean ready = isReady(stack);
        float growth = getGrowthPercent(stack);

        tooltip.add(Component.translatable("tooltip.chimneysmushrooms.mushroom_type")
                .append(": ")
                .append(Component.translatable("mushroom.chimneysmushrooms." + type.getName()))
                .withStyle(ChatFormatting.GRAY));

        if (ready) {
            tooltip.add(Component.translatable("tooltip.chimneysmushrooms.ready_to_plant")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
            tooltip.add(Component.translatable("tooltip.chimneysmushrooms.right_click_to_plant")
                    .withStyle(ChatFormatting.DARK_GRAY));
        } else {
            int percent = (int)(growth * 100);
            tooltip.add(Component.translatable("tooltip.chimneysmushrooms.growth", percent)
                    .withStyle(ChatFormatting.DARK_GREEN));

            String growthHint = type.getGrowthHint();
            if (!growthHint.isEmpty()) {
                tooltip.add(Component.translatable(growthHint)
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
            }
        }

        tooltip.add(Component.translatable("tooltip.chimneysmushrooms.shift_click_for_status")
                .withStyle(ChatFormatting.AQUA));
    }

    private MushroomType getMushroomTypeFromTag(CompoundTag tag) {
        try {
            return MushroomType.valueOf(tag.getString(MUSHROOM_TYPE_KEY));
        } catch (IllegalArgumentException e) {
            return MushroomType.BROWN;
        }
    }

    public static MushroomType getMushroomType(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        try {
            return MushroomType.valueOf(tag.getString(MUSHROOM_TYPE_KEY));
        } catch (IllegalArgumentException e) {
            return MushroomType.BROWN;
        }
    }

    public static float getGrowthPercent(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(GROWTH_PERCENT_KEY) ? tag.getFloat(GROWTH_PERCENT_KEY) : 0.0f;
    }

    public static boolean isReady(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean(READY_KEY);
    }
}
