package net.chimney.mushrooms.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


@JeiPlugin
public class JEIChimneysMushroomsPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.tryBuild(net.chimney.mushrooms.ChimneysMushrooms.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(ModItems.SPAWN_BAG.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.chimneysmushrooms.spawn_bag.info"));
        registration.addIngredientInfo(new ItemStack(ModItems.GRAIN_BAG.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.chimneysmushrooms.grain_bag.info"));
    }
}
