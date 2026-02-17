package net.chimney.mushrooms.datagen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {


    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.BAG.get())
                .requires(Items.STRING)
                .requires(Items.LEATHER)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(pWriter, new ResourceLocation("minecraft", "bundle"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GRAIN_BAG.get())
                .requires(ModItems.BAG.get())
                .requires(Ingredient.of(Tags.Items.SEEDS), 8)
                .unlockedBy(getHasName(ModItems.BAG.get()), has(ModItems.BAG.get()))
                .save(pWriter, modLoc("grain_bag_recipe"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.AMALGAMATION.get())
                .requires(ModItems.MAGICAL_MUSHROOM.get())
                .requires(ModItems.DEATH_CAP.get())
                .requires(ModItems.FLY_AGARIC.get())
                .requires(ModItems.RUSSULA.get())
                .requires(ModItems.DUNG.get())
                .unlockedBy(getHasName(ModItems.MAGICAL_MUSHROOM.get()), has(ModItems.MAGICAL_MUSHROOM.get()))
                .save(pWriter, modLoc("amalgamation"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.MULCH_BLOCK.get(),2)
                .requires(Blocks.MUD)
                .requires(ModItems.DUNG.get(), 2)
                .requires(Items.WHEAT, 2)
                .unlockedBy(getHasName(ModItems.DUNG.get()), has(ModItems.DUNG.get()))
                .save(pWriter, modLoc("mulch_recipe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DUNG_BLOCK.get())
                .pattern("DD")
                .pattern("DD")
                .define('D', ModItems.DUNG.get())
                .unlockedBy(getHasName(ModItems.DUNG.get()), has(ModItems.DUNG.get()))
                .save(pWriter, modLoc("dung_block_from_dung"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TURTLE_DUNG_BLOCK.get())
                .pattern("DD")
                .pattern("DD")
                .define('D', ModItems.TURTLE_DUNG.get())
                .unlockedBy(getHasName(ModItems.TURTLE_DUNG.get()), has(ModItems.TURTLE_DUNG.get()))
                .save(pWriter, modLoc("turtle_dung_block_from_dung"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DUNG.get(), 4)
                .requires(ModBlocks.DUNG_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.DUNG_BLOCK.get()), has(ModBlocks.DUNG_BLOCK.get()))
                .save(pWriter, modLoc("dung_from_dung_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TURTLE_DUNG.get(), 4)
                .requires(ModBlocks.TURTLE_DUNG_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.TURTLE_DUNG_BLOCK.get()), has(ModBlocks.TURTLE_DUNG_BLOCK.get()))
                .save(pWriter, modLoc("turtle_dung_from_dung_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.TURTLE_MULCH_BLOCK.get(),2)
                .requires(Blocks.MUD)
                .requires(ModItems.DUNG.get(), 2)
                .requires(Items.SCUTE, 1)
                .unlockedBy(getHasName(ModItems.DUNG.get()), has(ModItems.DUNG.get()))
                .save(pWriter, modLoc("turtle_mulch_recipe"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_PLANK.get(), 4)
                .requires(ModBlocks.MYCELIATED_LOG.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_LOG.get()), has(ModBlocks.MYCELIATED_LOG.get()))
                .save(pWriter, modLoc("myceliated_plank_from_log"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_PLANK.get(), 4)
                .requires(ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .unlockedBy(getHasName(ModBlocks.STRIPPED_MYCELIATED_LOG.get()), has(ModBlocks.STRIPPED_MYCELIATED_LOG.get()))
                .save(pWriter, modLoc("myceliated_plank_from_stripped_log"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_PLANK.get(), 4)
                .requires(ModBlocks.MYCELIATED_WOOD.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_WOOD.get()), has(ModBlocks.MYCELIATED_WOOD.get()))
                .save(pWriter, modLoc("myceliated_plank_from_wood"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_PLANK.get(), 4)
                .requires(ModBlocks.STRIPPED_MYCELIATED_WOOD.get())
                .unlockedBy(getHasName(ModBlocks.STRIPPED_MYCELIATED_WOOD.get()), has(ModBlocks.STRIPPED_MYCELIATED_WOOD.get()))
                .save(pWriter, modLoc("myceliated_plank_from_stripped_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_WOOD.get(), 3)
                .pattern("LL")
                .pattern("LL")
                .define('L', ModBlocks.MYCELIATED_LOG.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_LOG.get()), has(ModBlocks.MYCELIATED_LOG.get()))
                .save(pWriter, modLoc("myceliated_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_MYCELIATED_WOOD.get(), 3)
                .pattern("LL")
                .pattern("LL")
                .define('L', ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .unlockedBy(getHasName(ModBlocks.STRIPPED_MYCELIATED_LOG.get()), has(ModBlocks.STRIPPED_MYCELIATED_LOG.get()))
                .save(pWriter, modLoc("stripped_myceliated_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_STAIRS.get(), 4)
                .pattern("P  ")
                .pattern("PP ")
                .pattern("PPP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MYCELIATED_SLAB.get(), 6)
                .pattern("PPP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_FENCE.get(), 3)
                .pattern("PSP")
                .pattern("PSP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_FENCE_GATE.get())
                .pattern("SPS")
                .pattern("SPS")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_DOOR.get(), 3)
                .pattern("PP")
                .pattern("PP")
                .pattern("PP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_TRAPDOOR.get(), 2)
                .pattern("PPP")
                .pattern("PPP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_trapdoor"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_BUTTON.get())
                .requires(ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_button"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.MYCELIATED_PRESSURE_PLATE.get())
                .pattern("PP")
                .define('P', ModBlocks.MYCELIATED_PLANK.get())
                .unlockedBy(getHasName(ModBlocks.MYCELIATED_PLANK.get()), has(ModBlocks.MYCELIATED_PLANK.get()))
                .save(pWriter, modLoc("myceliated_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.MUSHROOM_DRYING_STATION.get())
                .pattern("S S")
                .pattern(" P ")
                .pattern("S S")
                .define('S', Items.STICK)
                .define('P', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(pWriter, modLoc("mushroom_drying_station"));

        addDriedMushroomRecipes(pWriter, "russula", ModItems.RUSSULA, ModItems.DRIED_RUSSULA);
        addDriedMushroomRecipes(pWriter, "blue_angel", ModItems.BLUE_ANGEL, ModItems.DRIED_BLUE_ANGEL);
        addDriedMushroomRecipes(pWriter, "chanterelle", ModItems.CHANTERELLE, ModItems.DRIED_CHANTERELLE);
        addDriedMushroomRecipes(pWriter, "magical_mushroom", ModItems.MAGICAL_MUSHROOM, ModItems.DRIED_MAGICAL_MUSHROOM);
        addDriedMushroomRecipes(pWriter, "lions_mane", ModItems.LIONS_MANE, ModItems.DRIED_LIONS_MANE);
        addDriedMushroomRecipes(pWriter, "oyster", ModItems.OYSTER, ModItems.DRIED_OYSTER);
        addDriedMushroomRecipes(pWriter, "shiitake", ModItems.SHIITAKE, ModItems.DRIED_SHIITAKE);
        addDriedMushroomRecipes(pWriter, "king_trumpet", ModItems.KING_TRUMPET, ModItems.DRIED_KING_TRUMPET);
        addDriedMushroomRecipes(pWriter, "portobello", ModItems.PORTOBELLO, ModItems.DRIED_PORTOBELLO);
        addDriedMushroomRecipes(pWriter, "morel", ModItems.MOREL, ModItems.DRIED_MOREL);
        addDriedMushroomRecipes(pWriter, "fly_agaric", ModItems.FLY_AGARIC, ModItems.DRIED_FLY_AGARIC);
        addDriedMushroomRecipes(pWriter, "death_cap", ModItems.DEATH_CAP, ModItems.DRIED_DEATH_CAP);
        addDriedMushroomRecipes(pWriter, "freedom_cap", ModItems.FREEDOM_CAP, ModItems.DRIED_FREEDOM_CAP);
        addDriedMushroomRecipes(pWriter, "brown_mushroom", Items.BROWN_MUSHROOM, ModItems.DRIED_BROWN_MUSHROOM);
        addDriedMushroomRecipes(pWriter, "red_mushroom", Items.RED_MUSHROOM, ModItems.DRIED_RED_MUSHROOM);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.RAW_BEEF_WELLINGTON.get())
                .pattern("MMM")
                .pattern(" B ")
                .pattern("WWW")
                .define('M', ModItems.PORTOBELLO.get())
                .define('B', Items.BEEF)
                .define('W', Items.WHEAT)
                .unlockedBy(getHasName(Items.BEEF), has(Items.BEEF))
                .save(pWriter, modLoc("raw_beef_wellington_recipe"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_BEEF_WELLINGTON.get()),
                        RecipeCategory.FOOD,
                        ModItems.BEEF_WELLINGTON.get(),
                        0.35f,
                        200)
                .unlockedBy(getHasName(ModItems.RAW_BEEF_WELLINGTON.get()), has(ModItems.RAW_BEEF_WELLINGTON.get()))
                .save(pWriter, modLoc("beef_wellington_from_smelting"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.RAW_BEEF_WELLINGTON.get()),
                        RecipeCategory.FOOD,
                        ModItems.BEEF_WELLINGTON.get(),
                        0.35f,
                        600)
                .unlockedBy(getHasName(ModItems.RAW_BEEF_WELLINGTON.get()), has(ModItems.RAW_BEEF_WELLINGTON.get()))
                .save(pWriter, modLoc("beef_wellington_from_campfire"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.RAW_MUSHROOM_SKEWER.get())
                .pattern("MMM")
                .pattern(" S ")
                .define('M', Ingredient.of(Tags.Items.MUSHROOMS))
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(pWriter, modLoc("raw_mushroom_skewer_recipe"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_MUSHROOM_SKEWER.get()),
                        RecipeCategory.FOOD,
                        ModItems.MUSHROOM_SKEWER.get(),
                        0.35f,
                        200)
                .unlockedBy(getHasName(ModItems.RAW_MUSHROOM_SKEWER.get()), has(ModItems.RAW_MUSHROOM_SKEWER.get()))
                .save(pWriter, modLoc("mushroom_skewer_from_smelting"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.RAW_MUSHROOM_SKEWER.get()),
                        RecipeCategory.FOOD,
                        ModItems.MUSHROOM_SKEWER.get(),
                        0.35f,
                        600)
                .unlockedBy(getHasName(ModItems.RAW_MUSHROOM_SKEWER.get()), has(ModItems.RAW_MUSHROOM_SKEWER.get()))
                .save(pWriter, modLoc("mushroom_skewer_from_campfire"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(Tags.Items.MUSHROOMS),
                        RecipeCategory.FOOD,
                        ModItems.MUSHROOM_JERKY.get(),
                        0.35f,
                        100)
                .unlockedBy(getHasName(ModItems.LIONS_MANE.get()), has(ModItems.LIONS_MANE.get()))
                .save(pWriter, modLoc("mushroom_jerky_from_smoking"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RUST_MUSHROOM_STEW.get())
                .requires(Items.BOWL)
                .requires(Ingredient.of(Tags.Items.MUSHROOMS), 3)
                .requires(Items.CARROT)
                .requires(Items.BAKED_POTATO)
                .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                .save(pWriter, modLoc("rustic_mushroom_stew_recipe"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MUSHROOM_JERKY.get(), 2)
                .requires(Ingredient.of(ModItemTagGenerator.DRIED_MUSHROOMS), 3)
                .unlockedBy(getHasName(ModItems.DRIED_CHANTERELLE.get()), has(ModItems.DRIED_CHANTERELLE.get()))
                .save(pWriter, modLoc("mushroom_jerky_from_dried_mushrooms"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RUST_MUSHROOM_STEW.get())
                .requires(Items.BOWL)
                .requires(Ingredient.of(ModItemTagGenerator.DRIED_MUSHROOMS), 2)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.POTATO)
                .unlockedBy(getHasName(ModItems.DRIED_CHANTERELLE.get()), has(ModItems.DRIED_CHANTERELLE.get()))
                .save(pWriter, modLoc("rustic_mushroom_stew_from_dried_mushrooms"));
    }

    private ResourceLocation modLoc(String path) {
        return new ResourceLocation(ChimneysMushrooms.MODID, path);
    }

    private void addDriedMushroomRecipes(Consumer<FinishedRecipe> writer, String name, RegistryObject<Item> fresh, RegistryObject<Item> dried) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(fresh.get()),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        200)
                .unlockedBy(getHasName(fresh.get()), has(fresh.get()))
                .save(writer, modLoc("dried_" + name + "_from_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(fresh.get()),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        100)
                .unlockedBy(getHasName(fresh.get()), has(fresh.get()))
                .save(writer, modLoc("dried_" + name + "_from_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(fresh.get()),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        600)
                .unlockedBy(getHasName(fresh.get()), has(fresh.get()))
                .save(writer, modLoc("dried_" + name + "_from_campfire"));
    }

    private void addDriedMushroomRecipes(Consumer<FinishedRecipe> writer, String name, Item fresh, RegistryObject<Item> dried) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(fresh),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        200)
                .unlockedBy(getHasName(fresh), has(fresh))
                .save(writer, modLoc("dried_" + name + "_from_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(fresh),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        100)
                .unlockedBy(getHasName(fresh), has(fresh))
                .save(writer, modLoc("dried_" + name + "_from_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(fresh),
                        RecipeCategory.FOOD,
                        dried.get(),
                        0.35f,
                        600)
                .unlockedBy(getHasName(fresh), has(fresh))
                .save(writer, modLoc("dried_" + name + "_from_campfire"));
    }

}
