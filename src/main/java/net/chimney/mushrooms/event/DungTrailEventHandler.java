package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID)
public class DungTrailEventHandler {

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(ModEffects.DUNG_TRAIL_EFFECT.get())) {
            return;
        }

        Level level = entity.level();

        if (level.isClientSide) {
            return;
        }

        if (entity.tickCount % ModCommonConfig.DUNG_TRAIL_PLACEMENT_INTERVAL_TICKS.get() != 0) {
            return;
        }

        BlockPos blockPos = entity.blockPosition();
        BlockState blockState = level.getBlockState(blockPos);

        if (level.isEmptyBlock(blockPos)) {
            BlockPos belowPos = blockPos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (belowState.isFaceSturdy(level, belowPos, net.minecraft.core.Direction.UP)) {
                if (entity instanceof Turtle) {
                    level.setBlockAndUpdate(blockPos, ModBlocks.TURTLE_DUNG_LAYER.get().defaultBlockState());
                } else {
                    level.setBlockAndUpdate(blockPos, ModBlocks.DUNG_LAYER.get().defaultBlockState());
                }
            }
        }
    }
}
