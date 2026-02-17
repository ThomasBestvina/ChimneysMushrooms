package net.chimney.mushrooms.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class DryingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int dryingTime; // In ticks (20 ticks = 1 second)

    public DryingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int dryingTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.dryingTime = dryingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.DRYING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.DRYING.get();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getDryingTime() {
        return this.dryingTime;
    }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {
        @Override
        public DryingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int dryingTime = GsonHelper.getAsInt(json, "dryingtime", 200); // Default 10 seconds

            return new DryingRecipe(recipeId, ingredient, result, dryingTime);
        }

        @Override
        public DryingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int dryingTime = buffer.readVarInt();

            return new DryingRecipe(recipeId, ingredient, result, dryingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DryingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.dryingTime);
        }
    }
}
