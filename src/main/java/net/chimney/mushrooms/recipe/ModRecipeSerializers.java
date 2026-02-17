package net.chimney.mushrooms.recipe;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChimneysMushrooms.MODID);

    public static final RegistryObject<RecipeSerializer<DryingRecipe>> DRYING_SERIALIZER =
            SERIALIZERS.register("drying", () -> new DryingRecipe.Serializer());
}
