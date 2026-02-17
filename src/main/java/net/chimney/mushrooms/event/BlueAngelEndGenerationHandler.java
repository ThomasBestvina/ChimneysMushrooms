package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlueAngelEndGenerationHandler {
    private static final String DATA_ID = ChimneysMushrooms.MODID + "_blue_angel_end_main_island";

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (level.dimension() != Level.END) {
            return;
        }

        BlueAngelEndData data = level.getDataStorage().computeIfAbsent(BlueAngelEndData::load, BlueAngelEndData::new, DATA_ID);
        if (data.generated) {
            return;
        }

        int minTarget = ModCommonConfig.BLUE_ANGEL_END_MIN_GUARANTEED.get();
        int maxTarget = Math.max(minTarget, ModCommonConfig.BLUE_ANGEL_END_MAX_GUARANTEED.get());
        int target = minTarget + level.random.nextInt((maxTarget - minTarget) + 1);
        int placed = 0;

        for (int i = 0; i < target; i++) {
            if (tryPlaceOne(level)) {
                placed++;
            }
        }

        if (placed < target) {
            for (int i = 0; i < ModCommonConfig.BLUE_ANGEL_END_PLACEMENT_RETRIES.get() && placed < target; i++) {
                int x = level.random.nextInt(61) - 30;
                int z = level.random.nextInt(61) - 30;
                if (tryPlaceAt(level, x, z)) {
                    placed++;
                }
            }
        }

        data.generated = true;
        data.setDirty();
    }

    private static boolean tryPlaceOne(ServerLevel level) {
        for (int attempts = 0; attempts < ModCommonConfig.BLUE_ANGEL_END_FIRST_PASS_ATTEMPTS.get(); attempts++) {
            int x = level.random.nextInt(61) - 30;
            int z = level.random.nextInt(61) - 30;

            if ((x * x + z * z) < (18 * 18)) {
                continue;
            }

            if (tryPlaceAt(level, x, z)) {
                return true;
            }
        }
        return false;
    }

    private static boolean tryPlaceAt(ServerLevel level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos below = pos.below();

        if (!level.getBlockState(pos).isAir()) {
            return false;
        }
        if (!level.getBlockState(below).is(Blocks.END_STONE)) {
            return false;
        }

        level.setBlock(pos, ModBlocks.BLUE_ANGEL_CLUSTER.get().defaultBlockState()
                .setValue(net.chimney.mushrooms.block.custom.MushroomClusterBlock.FACING, net.minecraft.core.Direction.UP)
                .setValue(net.chimney.mushrooms.block.custom.MushroomClusterBlock.GROWTH_STAGE, 3), 3);
        return true;
    }

    private static class BlueAngelEndData extends SavedData {
        private boolean generated;

        private BlueAngelEndData() {
            this.generated = false;
        }

        private static BlueAngelEndData load(CompoundTag tag) {
            BlueAngelEndData data = new BlueAngelEndData();
            data.generated = tag.getBoolean("generated");
            return data;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("generated", this.generated);
            return tag;
        }
    }
}
