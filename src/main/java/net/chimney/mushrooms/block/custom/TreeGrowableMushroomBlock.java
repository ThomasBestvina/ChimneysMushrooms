package net.chimney.mushrooms.block.custom;

import net.chimney.mushrooms.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class TreeGrowableMushroomBlock extends BushBlock implements BonemealableBlock {
    public TreeGrowableMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        Holder<ConfiguredFeature<?, ?>> feature = level.registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolderOrThrow(ModConfiguredFeatures.MYCELIATED_KEY);
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();

        level.removeBlock(pos, false);
        if (!feature.value().place(level, chunkGenerator, random, pos)) {
            level.setBlock(pos, state, 3);
        }
    }
}
