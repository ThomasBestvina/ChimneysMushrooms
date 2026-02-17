package net.chimney.mushrooms.item.custom;

import net.chimney.mushrooms.item.ModItems;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class GrainBagItem extends Item {
    public GrainBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("tooltip.chimneysmushrooms.grain_bag.usage")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.chimneysmushrooms.grain_bag.offhand")
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        MushroomType mushroomType = null;
        for (MushroomType type : MushroomType.values()) {
            if (type.canObtainFromBlock(block)) {
                mushroomType = type;
                break;
            }
        }

        if (mushroomType != null) {
            return inoculateFromBlock(context, mushroomType, stack, player);
        }

        return inoculateFromOffhand(level, player, stack);
    }

    private InteractionResult inoculateFromBlock(UseOnContext context, MushroomType type,
                                                 ItemStack grainBag, Player player) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide) {
            ItemStack spawnBag = createSpawnBag(type, player);

            grainBag.shrink(1);
            if (!player.getInventory().add(spawnBag)) {
                player.drop(spawnBag, false);
            }

            if (level.random.nextFloat() < 0.3f) {
                level.destroyBlock(pos, false);
            }

            level.playSound(null, pos,
                    SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS,
                    0.7f, 0.9f + level.random.nextFloat() * 0.2f);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        8, 0.3, 0.3, 0.3, 0.05);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResult inoculateFromOffhand(Level level, Player player, ItemStack grainBag) {
        ItemStack offhandItem = player.getOffhandItem();
        MushroomType mushroomType = null;

        for (MushroomType type : MushroomType.values()) {
            if (type.canObtainFromItem(offhandItem.getItem())) {
                mushroomType = type;
                break;
            }
        }

        if (mushroomType != null) {
            if (!level.isClientSide) {
                ItemStack spawnBag = createSpawnBag(mushroomType, player);

                grainBag.shrink(1);
                offhandItem.shrink(1);
                if (!player.getInventory().add(spawnBag)) {
                    player.drop(spawnBag, false);
                }

                level.playSound(null, player.blockPosition(),
                        SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS,
                        0.7f, 1.0f);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack grainBag = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(grainBag);
        }

        InteractionResult result = inoculateFromOffhand(level, player, grainBag);
        if (result.consumesAction()) {
            return InteractionResultHolder.sidedSuccess(grainBag, level.isClientSide);
        }

        return InteractionResultHolder.pass(grainBag);
    }

    private ItemStack createSpawnBag(MushroomType type, Player player) {
        ItemStack stack = new ItemStack(ModItems.SPAWN_BAG.get());
        CompoundTag tag = new CompoundTag();
        tag.putString("MushroomType", type.name());
        tag.putLong("StartTime", player.level().getGameTime());
        tag.putFloat("GrowthPercent", 0.0f);
        tag.putBoolean("Ready", false);
        stack.setTag(tag);
        return stack;
    }
}
