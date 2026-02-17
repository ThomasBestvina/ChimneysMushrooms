package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.item.ModItems;
import net.chimney.mushrooms.item.custom.SpawnBagItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SPAWN_BAG.get(),
                    new ResourceLocation(ChimneysMushrooms.MODID, "growth"),
                    (stack, level, entity, seed) -> SpawnBagItem.getGrowthPercent(stack));
        });
    }
}
