package net.chimney.mushrooms.recipe;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ChimneysMushrooms.MODID);

    public static final RegistryObject<RecipeType<DryingRecipe>> DRYING =
            RECIPE_TYPES.register("drying", () -> new RecipeType<DryingRecipe>() {
                @Override
                public String toString() {
                    return "drying";
                }
            });
}
