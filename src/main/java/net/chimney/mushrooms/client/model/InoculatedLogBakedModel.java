package net.chimney.mushrooms.client.model;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.custom.InoculatedLogBlock;
import net.chimney.mushrooms.block.custom.LogInoculationStage;
import net.chimney.mushrooms.block.entity.InoculatedLogBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InoculatedLogBakedModel extends BakedModelWrapper<BakedModel> {
    public InoculatedLogBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource random, @NotNull ModelData modelData,
                                             @Nullable RenderType renderType) {
        if (state == null || state.getValue(InoculatedLogBlock.STAGE) == LogInoculationStage.FULLY_COLONIZED) {
            return super.getQuads(state, side, random, modelData, renderType);
        }

        BlockState originalLogState = modelData.get(InoculatedLogBlockEntity.ORIGINAL_LOG_STATE);
        if (originalLogState == null || originalLogState.getBlock() == ModBlocks.INOCULATED_LOG.get()) {
            long seed = random.nextLong();
            BlockState oakState = Blocks.OAK_LOG.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, state.getValue(InoculatedLogBlock.AXIS));
            BakedModel oakModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(oakState);

            // While BE model-data is syncing, draw a temporary oak underlay plus inoculated overlay.
            // This prevents the near-invisible frame on cutout textures.
            List<BakedQuad> combined = new ArrayList<>(getModelQuads(
                    oakModel, oakState, side, seed, ModelData.EMPTY, renderType
            ));
            combined.addAll(getModelQuads(this.originalModel, state, side, seed, modelData, renderType));
            return combined;
        }

        BakedModel originalLogModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(originalLogState);
        long seed = random.nextLong();
        List<BakedQuad> combined = new ArrayList<>(getModelQuads(
                originalLogModel, originalLogState, side, seed, ModelData.EMPTY, renderType
        ));
        combined.addAll(getModelQuads(this.originalModel, state, side, seed, modelData, renderType));
        return combined;
    }

    private static List<BakedQuad> getModelQuads(BakedModel model, BlockState state, @Nullable Direction side,
                                                 long seed, ModelData modelData, @Nullable RenderType renderType) {
        List<BakedQuad> quads = model.getQuads(state, side, RandomSource.create(seed), modelData, renderType);
        if (quads.isEmpty() && renderType != null) {
            return model.getQuads(state, side, RandomSource.create(seed), modelData, null);
        }
        return quads;
    }
}
