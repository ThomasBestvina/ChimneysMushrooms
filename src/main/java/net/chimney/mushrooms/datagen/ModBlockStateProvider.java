package net.chimney.mushrooms.datagen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.custom.*;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper){
        super(output, ChimneysMushrooms.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        inoculatedSubstrateBlockState();
        inoculatedMulchBlockState();
        inoculatedTurtleMulchBlockState();
        inoculatedLogBlockState();
        myceliatedLogBlockState();
        strippedMyceliatedLogBlockState();
        myceliatedWoodBlockState();
        strippedMyceliatedWoodBlockState();
        myceliatedBuildingBlockStates();

        dungLayerBlockState();
        turtleDungLayerBlockState();

        blockWithItem(ModBlocks.MULCH_BLOCK);
        blockWithItem(ModBlocks.TURTLE_MULCH_BLOCK);
        blockWithItem(ModBlocks.DUNG_BLOCK);
        blockWithItem(ModBlocks.TURTLE_DUNG_BLOCK);
        blockWithItem(ModBlocks.MYCELIATED_DIRT);

        simpleBlock(ModBlocks.RUSSULA_BLOCK.get(),
                models().cross("russula_block", modLoc("block/russula_block")).renderType("cutout"));

        simpleBlock(ModBlocks.CHANTERELLE_BLOCK.get(),
                models().cross("chanterelle_block", modLoc("block/chanterelle_block")).renderType("cutout"));

        simpleBlock(ModBlocks.MAGICAL_MUSHROOM_BLOCK.get(),
                models().cross("magical_mushroom_block", modLoc("block/magical_mushroom_block")).renderType("cutout"));

        simpleBlock(ModBlocks.OYSTER_BLOCK.get(),
                models().cross("oyster_block", modLoc("block/oyster_block")).renderType("cutout"));

        simpleBlock(ModBlocks.SHIITAKE_BLOCK.get(),
                models().cross("shiitake_block", modLoc("block/shiitake_block")).renderType("cutout"));

        simpleBlock(ModBlocks.KING_TRUMPET_BLOCK.get(),
                models().cross("king_trumpet_block", modLoc("block/king_trumpet_block")).renderType("cutout"));

        simpleBlock(ModBlocks.PORTOBELLO_BLOCK.get(),
                models().cross("portobello_block", modLoc("block/portobello_block")).renderType("cutout"));

        simpleBlock(ModBlocks.MOREL_BLOCK.get(),
                models().cross("morel_block", modLoc("block/morel_block")).renderType("cutout"));

        simpleBlock(ModBlocks.FLY_AGARIC_BLOCK.get(),
                models().cross("fly_agaric_block", modLoc("block/fly_agaric_block")).renderType("cutout"));

        simpleBlock(ModBlocks.DEATH_CAP_BLOCK.get(),
                models().cross("death_cap_block", modLoc("block/death_cap_block")).renderType("cutout"));

        simpleBlock(ModBlocks.FREEDOM_CAP_BLOCK.get(),
                models().cross("freedom_cap_block", modLoc("block/freedom_cap_block")).renderType("cutout"));


        mushroomClusterBlockState(ModBlocks.BLUE_ANGEL_CLUSTER, "blue_angel");
        mushroomClusterBlockState(ModBlocks.LIONS_MANE_CLUSTER, "lions_mane");
        mushroomClusterBlockState(ModBlocks.BROWN_MUSHROOM_CLUSTER, "brown_mushroom");
        mushroomClusterBlockState(ModBlocks.RED_MUSHROOM_CLUSTER, "red_mushroom");
        mushroomClusterBlockState(ModBlocks.RUSSULA_CLUSTER, "russula");
        mushroomClusterBlockState(ModBlocks.CHANTERELLE_CLUSTER, "chanterelle");
        mushroomClusterBlockState(ModBlocks.MAGICAL_MUSHROOM_CLUSTER, "magical_mushroom");
        mushroomClusterBlockState(ModBlocks.OYSTER_CLUSTER, "oyster");
        mushroomClusterBlockState(ModBlocks.SHIITAKE_CLUSTER, "shiitake");
        mushroomClusterBlockState(ModBlocks.KING_TRUMPET_CLUSTER, "king_trumpet");
        mushroomClusterBlockState(ModBlocks.PORTOBELLO_CLUSTER, "portobello");
        mushroomClusterBlockState(ModBlocks.MOREL_CLUSTER, "morel");
        mushroomClusterBlockState(ModBlocks.FLY_AGARIC_CLUSTER, "fly_agaric");
        mushroomClusterBlockState(ModBlocks.DEATH_CAP_CLUSTER, "death_cap");
        mushroomClusterBlockState(ModBlocks.FREEDOM_CAP_CLUSTER, "freedom_cap");
    }

    private void inoculatedSubstrateBlockState() {
        ModelFile stage0Model = models().cubeAll("inoculated_substrate_0",
                modLoc("block/inoculated_substrate_0"));
        ModelFile stage1Model = models().cubeAll("inoculated_substrate_1",
                modLoc("block/inoculated_substrate_1"));
        ModelFile stage2Model = models().cubeTop("inoculated_substrate_2",
                modLoc("block/inoculated_substrate_2_side"),
                modLoc("block/inoculated_substrate_2_top"));

        ModelFile simpleModel = models().cubeAll("inoculated_substrate",
                modLoc("block/inoculated_substrate_0"));

        getVariantBuilder(ModBlocks.INOCULATED_SUBSTRATE.get())
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.INOCULATED)
                .modelForState().modelFile(stage0Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.IN_PROGRESS)
                .modelForState().modelFile(stage1Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.FULLY_INOCULATED)
                .modelForState().modelFile(stage2Model).addModel();

        itemModels().withExistingParent("inoculated_substrate",
                modLoc("block/inoculated_substrate"));

        saplingBlock(ModBlocks.MYCELIATED_SAPLING);
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject)
    {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void inoculatedMulchBlockState() {
        ModelFile stage0Model = models().cubeAll("inoculated_mulch_0",
                modLoc("block/inoculated_mulch_0"));
        ModelFile stage1Model = models().cubeAll("inoculated_mulch_1",
                modLoc("block/inoculated_mulch_1"));
        ModelFile stage2Model = models().cubeTop("inoculated_mulch_2",
                modLoc("block/inoculated_mulch_2_side"),
                modLoc("block/inoculated_mulch_2_top"));

        ModelFile simpleModel = models().cubeAll("inoculated_mulch",
                modLoc("block/inoculated_mulch_0"));

        getVariantBuilder(ModBlocks.INOCULATED_MULCH.get())
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.INOCULATED)
                .modelForState().modelFile(stage0Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.IN_PROGRESS)
                .modelForState().modelFile(stage1Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.FULLY_INOCULATED)
                .modelForState().modelFile(stage2Model).addModel();

        itemModels().withExistingParent("inoculated_mulch",
                modLoc("block/inoculated_mulch"));
    }

    private void inoculatedTurtleMulchBlockState() {
        ModelFile stage0Model = models().cubeAll("inoculated_turtle_mulch_0",
                modLoc("block/inoculated_turtle_mulch_0"));
        ModelFile stage1Model = models().cubeAll("inoculated_turtle_mulch_1",
                modLoc("block/inoculated_turtle_mulch_1"));
        ModelFile stage2Model = models().cubeTop("inoculated_turtle_mulch_2",
                modLoc("block/inoculated_turtle_mulch_2_side"),
                modLoc("block/inoculated_turtle_mulch_2_top"));

        ModelFile simpleModel = models().cubeAll("inoculated_turtle_mulch",
                modLoc("block/inoculated_turtle_mulch_0"));

        getVariantBuilder(ModBlocks.INOCULATED_TURTLE_MULCH.get())
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.INOCULATED)
                .modelForState().modelFile(stage0Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.IN_PROGRESS)
                .modelForState().modelFile(stage1Model).addModel()
                .partialState().with(InoculatedSubstrateBlock.STAGE,
                        net.chimney.mushrooms.block.custom.InoculationStage.FULLY_INOCULATED)
                .modelForState().modelFile(stage2Model).addModel();

        itemModels().withExistingParent("inoculated_turtle_mulch",
                modLoc("block/inoculated_turtle_mulch"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void dungLayerBlockState() {
        ResourceLocation texture = modLoc("block/dung_layer");

        models().cubeAll("dung_layer", texture);

        for (int layers = 1; layers <= 8; layers++) {
            String modelName = "dung_layer_height" + (layers * 2);

            if (layers == 8) {
                models().cubeAll(modelName, texture);
            } else {
                models().withExistingParent(modelName, mcLoc("block/snow_height" + (layers * 2)))
                        .texture("particle", texture)
                        .texture("texture", texture);
            }
        }

        getVariantBuilder(ModBlocks.DUNG_LAYER.get())
                .forAllStates(state -> {
                    int layers = state.getValue(SnowLayerBlock.LAYERS);

                    if (layers == 0) {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("dung_layer_height2")))
                                .build();
                    } else if (layers == 8) {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("dung_layer_height16")))
                                .build();
                    } else {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("dung_layer_height" + (layers * 2))))
                                .build();
                    }
                });
        itemModels().withExistingParent("dung_layer", modLoc("block/dung_layer"));
    }

    private void turtleDungLayerBlockState() {
        ResourceLocation texture = modLoc("block/turtle_dung_layer");

        models().cubeAll("turtle_dung_layer", texture);

        for (int layers = 1; layers <= 8; layers++) {
            String modelName = "turtle_dung_layer_height" + (layers * 2);

            if (layers == 8) {
                models().cubeAll(modelName, texture);
            } else {
                models().withExistingParent(modelName, mcLoc("block/snow_height" + (layers * 2)))
                        .texture("particle", texture)
                        .texture("texture", texture);
            }
        }

        getVariantBuilder(ModBlocks.TURTLE_DUNG_LAYER.get())
                .forAllStates(state -> {
                    int layers = state.getValue(SnowLayerBlock.LAYERS);

                    if (layers == 0) {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("turtle_dung_layer_height2")))
                                .build();
                    } else if (layers == 8) {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("turtle_dung_layer_height16")))
                                .build();
                    } else {
                        return ConfiguredModel.builder()
                                .modelFile(models().getExistingFile(modLoc("turtle_dung_layer_height" + (layers * 2))))
                                .build();
                    }
                });
        itemModels().withExistingParent("turtle_dung_layer", modLoc("block/turtle_dung_layer"));
    }

    private void inoculatedLogBlockState() {
        ResourceLocation topStage0 = modLoc("block/inoculated_substrate_log_0");
        ResourceLocation sideStage0 = modLoc("block/inoculated_substrate_log_0");

        ResourceLocation topStage1 = modLoc("block/inoculated_substrate_log_1");
        ResourceLocation sideStage1 = modLoc("block/inoculated_substrate_log_1");

        ResourceLocation topStage2 = modLoc("block/inoculated_substrate_log_2_top");
        ResourceLocation sideStage2 = modLoc("block/inoculated_substrate_log_2_side");

        ModelFile stage0Vertical = models().cubeColumn("inoculated_log_0",
                sideStage0, topStage0).renderType("cutout");

        ModelFile stage1Vertical = models().cubeColumn("inoculated_log_1",
                sideStage1, topStage1).renderType("cutout");

        ModelFile stage2Vertical = models().cubeColumn("inoculated_log_2",
                sideStage2, topStage2).renderType("cutout");
        models().cubeColumnHorizontal("inoculated_log_0_horizontal", sideStage0, topStage0).renderType("cutout");
        models().cubeColumnHorizontal("inoculated_log_1_horizontal", sideStage1, topStage1).renderType("cutout");
        models().cubeColumnHorizontal("inoculated_log_2_horizontal", sideStage2, topStage2).renderType("cutout");

        getVariantBuilder(ModBlocks.INOCULATED_LOG.get())
                .forAllStates(state -> {
                    LogInoculationStage stage = state.getValue(InoculatedLogBlock.STAGE);
                    Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);

                    ModelFile model;
                    if (axis == Direction.Axis.Y) {
                        model = switch (stage) {
                            case INOCULATED -> stage0Vertical;
                            case COLONIZING -> stage1Vertical;
                            case FULLY_COLONIZED -> stage2Vertical;
                        };
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .build();
                    } else {
                        model = switch (stage) {
                            case INOCULATED -> stage0Vertical;
                            case COLONIZING -> stage1Vertical;
                            case FULLY_COLONIZED -> stage2Vertical;
                        };
                        int rotationY = axis == Direction.Axis.X ? 90 : 0;
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .rotationX(90)
                                .rotationY(rotationY)
                                .build();
                    }
                });
        itemModels().cubeColumn("inoculated_log", sideStage0, topStage0);
    }

    private void myceliatedLogBlockState() {
        ResourceLocation end = modLoc("block/inoculated_substrate_log_2_top");
        ResourceLocation side = modLoc("block/inoculated_substrate_log_2_side");

        ModelFile vertical = models().cubeColumn("myceliated_log", side, end);
        models().cubeColumnHorizontal("myceliated_log_horizontal", side, end);

        getVariantBuilder(ModBlocks.MYCELIATED_LOG.get())
                .forAllStates(state -> {
                    Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
                    if (axis == Direction.Axis.Y) {
                        return ConfiguredModel.builder().modelFile(vertical).build();
                    }
                    int rotationY = axis == Direction.Axis.X ? 90 : 0;
                    return ConfiguredModel.builder()
                            .modelFile(vertical)
                            .rotationX(90)
                            .rotationY(rotationY)
                            .build();
                });

        itemModels().withExistingParent("myceliated_log", modLoc("block/myceliated_log"));
    }

    private void strippedMyceliatedLogBlockState() {
        ResourceLocation end = modLoc("block/stripped_myceliated_log_top");
        ResourceLocation side = modLoc("block/stripped_myceliated_log");

        ModelFile vertical = models().cubeColumn("stripped_myceliated_log", side, end);

        getVariantBuilder(ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .forAllStates(state -> {
                    Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
                    if (axis == Direction.Axis.Y) {
                        return ConfiguredModel.builder().modelFile(vertical).build();
                    }
                    int rotationY = axis == Direction.Axis.X ? 90 : 0;
                    return ConfiguredModel.builder()
                            .modelFile(vertical)
                            .rotationX(90)
                            .rotationY(rotationY)
                            .build();
                });

        itemModels().withExistingParent("stripped_myceliated_log", modLoc("block/stripped_myceliated_log"));
    }

    private void myceliatedWoodBlockState() {
        ResourceLocation texture = modLoc("block/myceliated_log");
        ModelFile vertical = models().cubeColumn("myceliated_wood", texture, texture);

        getVariantBuilder(ModBlocks.MYCELIATED_WOOD.get())
                .forAllStates(state -> {
                    Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
                    if (axis == Direction.Axis.Y) {
                        return ConfiguredModel.builder().modelFile(vertical).build();
                    }
                    int rotationY = axis == Direction.Axis.X ? 90 : 0;
                    return ConfiguredModel.builder()
                            .modelFile(vertical)
                            .rotationX(90)
                            .rotationY(rotationY)
                            .build();
                });

        itemModels().withExistingParent("myceliated_wood", modLoc("block/myceliated_wood"));
    }

    private void strippedMyceliatedWoodBlockState() {
        ResourceLocation texture = modLoc("block/stripped_myceliated_log");
        ModelFile vertical = models().cubeColumn("stripped_myceliated_wood", texture, texture);

        getVariantBuilder(ModBlocks.STRIPPED_MYCELIATED_WOOD.get())
                .forAllStates(state -> {
                    Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
                    if (axis == Direction.Axis.Y) {
                        return ConfiguredModel.builder().modelFile(vertical).build();
                    }
                    int rotationY = axis == Direction.Axis.X ? 90 : 0;
                    return ConfiguredModel.builder()
                            .modelFile(vertical)
                            .rotationX(90)
                            .rotationY(rotationY)
                            .build();
                });

        itemModels().withExistingParent("stripped_myceliated_wood", modLoc("block/stripped_myceliated_wood"));
    }

    private void myceliatedBuildingBlockStates() {
        ResourceLocation plankTexture = modLoc("block/myceliated_plank");
        ResourceLocation doorBottomTexture = modLoc("block/myceliated_door_bottom");
        ResourceLocation doorTopTexture = modLoc("block/myceliated_door_top");
        simpleBlockWithItem(ModBlocks.MYCELIATED_PLANK.get(), models().cubeAll("myceliated_plank", plankTexture));

        stairsBlock((net.minecraft.world.level.block.StairBlock) ModBlocks.MYCELIATED_STAIRS.get(), plankTexture);
        slabBlock((net.minecraft.world.level.block.SlabBlock) ModBlocks.MYCELIATED_SLAB.get(), plankTexture, plankTexture);
        fenceBlock((net.minecraft.world.level.block.FenceBlock) ModBlocks.MYCELIATED_FENCE.get(), plankTexture);
        fenceGateBlock((net.minecraft.world.level.block.FenceGateBlock) ModBlocks.MYCELIATED_FENCE_GATE.get(), plankTexture);
        buttonBlock((net.minecraft.world.level.block.ButtonBlock) ModBlocks.MYCELIATED_BUTTON.get(), plankTexture);
        pressurePlateBlock((net.minecraft.world.level.block.PressurePlateBlock) ModBlocks.MYCELIATED_PRESSURE_PLATE.get(), plankTexture);
        doorBlockWithRenderType((net.minecraft.world.level.block.DoorBlock) ModBlocks.MYCELIATED_DOOR.get(),
                doorBottomTexture, doorTopTexture, "cutout");
        trapdoorBlockWithRenderType((net.minecraft.world.level.block.TrapDoorBlock) ModBlocks.MYCELIATED_TRAPDOOR.get(),
                modLoc("block/myceliated_trapdoor"), true, "cutout");

        simpleBlockItem(ModBlocks.MYCELIATED_STAIRS.get(), models().getExistingFile(modLoc("block/myceliated_stairs")));
        simpleBlockItem(ModBlocks.MYCELIATED_SLAB.get(), models().getExistingFile(modLoc("block/myceliated_slab")));
        itemModels().withExistingParent("myceliated_fence", mcLoc("block/fence_inventory"))
                .texture("texture", plankTexture);
        simpleBlockItem(ModBlocks.MYCELIATED_FENCE_GATE.get(), models().getExistingFile(modLoc("block/myceliated_fence_gate")));
        itemModels().withExistingParent("myceliated_button", mcLoc("block/button_inventory"))
                .texture("texture", plankTexture);
        simpleBlockItem(ModBlocks.MYCELIATED_PRESSURE_PLATE.get(),
                models().getExistingFile(modLoc("block/myceliated_pressure_plate")));
        itemModels().basicItem(modLoc("myceliated_door"));
        simpleBlockItem(ModBlocks.MYCELIATED_TRAPDOOR.get(), models().getExistingFile(modLoc("block/myceliated_trapdoor_bottom")));
    }

    private void mushroomClusterBlockState(RegistryObject<Block> block, String mushroomType) {
        ModelFile[] clusterModels = new ModelFile[4];
        for (int i = 0; i < 4; i++) {
            clusterModels[i] = models().cross(mushroomType + "_cluster_" + i,
                            modLoc("block/" + mushroomType + "_cluster_" + i))
                    .renderType("cutout");
        }

        getVariantBuilder(block.get())
                .forAllStates(state -> {
                    int growthStage = state.getValue(net.chimney.mushrooms.block.custom.MushroomClusterBlock.GROWTH_STAGE);
                    net.minecraft.core.Direction facing = state.getValue(net.chimney.mushrooms.block.custom.MushroomClusterBlock.FACING);

                    ConfiguredModel.Builder<?> builder = ConfiguredModel.builder()
                            .modelFile(clusterModels[Math.min(growthStage, 3)]); // Cap at 3

                    switch (facing) {
                        case DOWN -> builder.rotationX(180);
                        case NORTH -> builder.rotationX(90);
                        case SOUTH -> builder.rotationX(90).rotationY(180);
                        case WEST -> builder.rotationX(90).rotationY(270);
                        case EAST -> builder.rotationX(90).rotationY(90);
                        default -> {}
                    }

                    return builder.build();
                });

        itemModels().withExistingParent(mushroomType + "_cluster",
                modLoc("block/" + mushroomType + "_cluster_0"));
    }

}
