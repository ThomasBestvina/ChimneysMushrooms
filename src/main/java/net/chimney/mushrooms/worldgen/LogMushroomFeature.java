package net.chimney.mushrooms.worldgen;

import com.mojang.serialization.Codec;
import net.chimney.mushrooms.block.custom.LogMushroomClusterBlock;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class LogMushroomFeature extends Feature<SimpleBlockConfiguration> {
    public LogMushroomFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        SimpleBlockConfiguration config = context.config();

        int searchRadius = 3;
        int searchHeight = 5;

        for (int y = -searchHeight; y <= searchHeight; y++) {
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos searchPos = pos.offset(x, y, z);

                    if (MushroomType.isAnyWoodBlock(level.getBlockState(searchPos).getBlock())) {
                        if (tryPlaceOnLog(level, searchPos, config, random)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean tryPlaceOnLog(WorldGenLevel level, BlockPos logPos, SimpleBlockConfiguration config, RandomSource random) {
        Direction[] horizontalDirections = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        for (int i = horizontalDirections.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Direction temp = horizontalDirections[i];
            horizontalDirections[i] = horizontalDirections[j];
            horizontalDirections[j] = temp;
        }

        for (Direction direction : horizontalDirections) {
            BlockPos placePos = logPos.relative(direction);

            if (level.isEmptyBlock(placePos) || level.getBlockState(placePos).canBeReplaced()) {
                BlockState mushroomState = config.toPlace().getState(random, placePos);

                if (mushroomState.hasProperty(LogMushroomClusterBlock.FACING)) {
                    mushroomState = mushroomState.setValue(LogMushroomClusterBlock.FACING, direction);
                }

                level.setBlock(placePos, mushroomState, 2);
                return true;
            }
        }

        return false;
    }
}
