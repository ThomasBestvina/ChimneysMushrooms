package net.chimney.mushrooms.datagen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChimneysMushrooms.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleSpawnBagItem(ModItems.SPAWN_BAG);
        simpleItemWithVanillaTexture(ModItems.BAG, "item/bundle");
        simpleItem(ModItems.GRAIN_BAG);
        simpleItem(ModItems.DRIED_BROWN_MUSHROOM);
        simpleItem(ModItems.DRIED_RED_MUSHROOM);
        simpleItem(ModItems.LIONS_MANE);
        simpleItem(ModItems.DRIED_LIONS_MANE);
        simpleItem(ModItems.RUSSULA);
        simpleItem(ModItems.DUNG);
        simpleItem(ModItems.TURTLE_DUNG);
        simpleItem(ModItems.AMALGAMATION);
        simpleItem(ModItems.BLUE_ANGEL);
        simpleItem(ModItems.DRIED_BLUE_ANGEL);
        simpleItem(ModItems.MAGICAL_MUSHROOM);
        simpleItem(ModItems.DRIED_MAGICAL_MUSHROOM);
        simpleItem(ModItems.OYSTER);
        simpleItem(ModItems.DRIED_OYSTER);
        simpleItem(ModItems.SHIITAKE);
        simpleItem(ModItems.DRIED_SHIITAKE);
        simpleItem(ModItems.KING_TRUMPET);
        simpleItem(ModItems.DRIED_KING_TRUMPET);
        simpleItem(ModItems.PORTOBELLO);
        simpleItem(ModItems.DRIED_PORTOBELLO);
        simpleItem(ModItems.MOREL);
        simpleItem(ModItems.DRIED_MOREL);
        simpleItem(ModItems.FLY_AGARIC);
        simpleItem(ModItems.DRIED_FLY_AGARIC);
        simpleItem(ModItems.DEATH_CAP);
        simpleItem(ModItems.DRIED_DEATH_CAP);
        simpleItem(ModItems.FREEDOM_CAP);
        simpleItem(ModItems.DRIED_FREEDOM_CAP);
        simpleItem(ModItems.CHANTERELLE);
        simpleItem(ModItems.DRIED_CHANTERELLE);
        simpleItem(ModItems.DRIED_RUSSULA);
        simpleItem(ModItems.RAW_BEEF_WELLINGTON);
        simpleItem(ModItems.BEEF_WELLINGTON);
        simpleItem(ModItems.RAW_MUSHROOM_SKEWER);
        simpleItem(ModItems.MUSHROOM_SKEWER);
        simpleItem(ModItems.MUSHROOM_JERKY);
        simpleItem(ModItems.RUST_MUSHROOM_STEW);

        if (ModItems.FARMERS_DELIGHT_LOADED) {
            simpleItem(ModItems.MUSHROOM_RISOTTO);
            simpleItem(ModItems.CREAMY_MUSHROOM_PASTA);
            simpleItem(ModItems.STUFFED_MUSHROOMS);
        }

        simpleBlockItemFromBlock(ModBlocks.DUNG_BLOCK);
        simpleBlockItemFromBlock(ModBlocks.MULCH_BLOCK);



        dungLayerItemModel();

        mushroomClusterItem(ModBlocks.LIONS_MANE_CLUSTER);
        mushroomClusterItem(ModBlocks.BROWN_MUSHROOM_CLUSTER);
        mushroomClusterItem(ModBlocks.RED_MUSHROOM_CLUSTER);
        mushroomClusterItem(ModBlocks.RUSSULA_CLUSTER);
        mushroomClusterItem(ModBlocks.BLUE_ANGEL_CLUSTER);
        mushroomClusterItem(ModBlocks.CHANTERELLE_CLUSTER);
        mushroomClusterItem(ModBlocks.MAGICAL_MUSHROOM_CLUSTER);
        mushroomClusterItem(ModBlocks.OYSTER_CLUSTER);
        mushroomClusterItem(ModBlocks.SHIITAKE_CLUSTER);
        mushroomClusterItem(ModBlocks.KING_TRUMPET_CLUSTER);
        mushroomClusterItem(ModBlocks.PORTOBELLO_CLUSTER);
        mushroomClusterItem(ModBlocks.MOREL_CLUSTER);
        mushroomClusterItem(ModBlocks.FLY_AGARIC_CLUSTER);
        mushroomClusterItem(ModBlocks.DEATH_CAP_CLUSTER);
        mushroomClusterItem(ModBlocks.FREEDOM_CAP_CLUSTER);

        simpleBlockItemFromBlock(ModBlocks.INOCULATED_SUBSTRATE);
        simpleBlockItemFromBlock(ModBlocks.INOCULATED_MULCH);

        saplingItem(ModBlocks.MYCELIATED_SAPLING);
    }

    private ItemModelBuilder mushroomClusterItem(RegistryObject<Block> block) {
        String path = block.getId().getPath();
        return withExistingParent(path,
                ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + path + "_3"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item)
    {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ChimneysMushrooms.MODID, "block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItemWithTexture(RegistryObject<Item> item, String textureName) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + textureName));
    }

    private ItemModelBuilder simpleItemWithVanillaTexture(RegistryObject<Item> item, String texturePath) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0", ResourceLocation.tryParse("minecraft:" + texturePath));
    }

    private void dungLayerItemModel() {
        withExistingParent("dung_layer", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/dung_layer"));
        withExistingParent("turtle_dung_layer", mcLoc("item/generated"))
                .texture("layer0", modLoc("block/turtle_dung_layer"));
    }

    private ItemModelBuilder simpleBlockItemFromBlock(RegistryObject<Block> block) {
        String path = block.getId().getPath();

        if (path.equals("inoculated_substrate")) {
            return withExistingParent(path,
                    ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/inoculated_substrate"));
        }

        return withExistingParent(path,
                ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + path));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Item> item) {
        if (item.getId().getPath().equals("brown_mushroom_cluster")) {
            return withExistingParent(item.getId().getPath(),
                    ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + item.getId().getPath() + "_0"));
        }

        if (item.getId().getPath().equals("red_mushroom_cluster")) {
            return withExistingParent(item.getId().getPath(),
                    ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + item.getId().getPath() + "_0"));
        }

        if (item.getId().getPath().equals("lions_mane_cluster")) {
            return withExistingParent(item.getId().getPath(),
                    ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + item.getId().getPath() + "_0"));
        }


        if (item.getId().getPath().equals("inoculated_substrate")) {
            return withExistingParent(item.getId().getPath(),
                    ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/inoculated_substrate"));
        }

        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "block/" + item.getId().getPath()));
    }

    private void simpleSpawnBagItem(RegistryObject<Item> item) {
        String path = item.getId().getPath();
        ModelFile stage0 = withExistingParent(path + "_stage_0",
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + path + "_stage_0"));

        ModelFile stage1 = withExistingParent(path + "_stage_1",
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + path + "_stage_1"));

        ModelFile stage2 = withExistingParent(path + "_stage_2",
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + path + "_stage_2"));
        withExistingParent(path,
                ResourceLocation.tryParse("item/generated"))
                .texture("layer0",
                        ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + path))
                .override()
                .predicate(ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "growth"), 0.0f)
                .model(getExistingFile(ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "item/" + path)))
                .end()
                .override()
                .predicate(ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "growth"), 0.33f)
                .model(stage0)
                .end()
                .override()
                .predicate(ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "growth"), 0.66f)
                .model(stage1)
                .end()
                .override()
                .predicate(ResourceLocation.tryBuild(ChimneysMushrooms.MODID, "growth"), 1.0f)
                .model(stage2)
                .end();
    }
}
