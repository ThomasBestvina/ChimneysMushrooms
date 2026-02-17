package net.chimney.mushrooms.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class DungLayerBlock extends SnowLayerBlock {
    public DungLayerBlock(Properties pProperties) {
        super(pProperties);
    }
}
