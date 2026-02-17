package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.client.model.InoculatedLogBakedModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModelEvents {
    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> models = event.getModels();

        for (BlockState state : ModBlocks.INOCULATED_LOG.get().getStateDefinition().getPossibleStates()) {
            ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
            BakedModel existing = models.get(modelLocation);
            if (existing != null) {
                models.put(modelLocation, new InoculatedLogBakedModel(existing));
            }
        }
    }
}
