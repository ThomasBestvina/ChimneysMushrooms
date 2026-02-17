package net.chimney.mushrooms.block;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.custom.*;
import net.chimney.mushrooms.item.ModItems;
import net.chimney.mushrooms.worldgen.tree.MyceliatedTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ChimneysMushrooms.MODID);

    public static final RegistryObject<Block> INOCULATED_SUBSTRATE = registerBlock("inoculated_substrate",
            () -> new InoculatedSubstrateBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.5f)
                    .sound(SoundType.WOOD)
                    .randomTicks()));

    public static final RegistryObject<Block> INOCULATED_MULCH = registerBlock("inoculated_mulch",
            () -> new InoculatedMulchBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)
                    .randomTicks()
                    .strength(0.5f)));

    public static final RegistryObject<Block> INOCULATED_TURTLE_MULCH = registerBlock("inoculated_turtle_mulch",
            () -> new InoculatedTurtleMulchBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)
                    .randomTicks()
                    .strength(0.5f)));

    public static final RegistryObject<Block> BROWN_MUSHROOM_CLUSTER = registerBlock("brown_mushroom_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> RED_MUSHROOM_CLUSTER = registerBlock("red_mushroom_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> INOCULATED_LOG = registerBlock("inoculated_log",
            () -> new InoculatedLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    .sound(SoundType.WOOD)
                    .randomTicks()
                    .strength(2.0f)));

    public static final RegistryObject<Block> STRIPPED_MYCELIATED_LOG = registerBlock("stripped_myceliated_log",
            () -> new MyceliatedLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)
                    .sound(SoundType.WOOD)
                    .strength(2.0f), null));

    public static final RegistryObject<Block> MYCELIATED_LOG = registerBlock("myceliated_log",
            () -> new MyceliatedLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    .sound(SoundType.WOOD)
                    .strength(2.0f), () -> STRIPPED_MYCELIATED_LOG.get()));

    public static final RegistryObject<Block> MYCELIATED_PLANK = registerBlock("myceliated_plank",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .sound(SoundType.WOOD)
                    .strength(2.0f, 3.0f)));

    public static final RegistryObject<Block> STRIPPED_MYCELIATED_WOOD = registerBlock("stripped_myceliated_wood",
            () -> new MyceliatedLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2.0f), null));

    public static final RegistryObject<Block> MYCELIATED_WOOD = registerBlock("myceliated_wood",
            () -> new MyceliatedLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)
                    .sound(SoundType.WOOD)
                    .strength(2.0f), () -> STRIPPED_MYCELIATED_WOOD.get()));

    public static final RegistryObject<Block> MYCELIATED_STAIRS = registerBlock("myceliated_stairs",
            () -> new StairBlock(() -> MYCELIATED_PLANK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));

    public static final RegistryObject<Block> MYCELIATED_SLAB = registerBlock("myceliated_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    public static final RegistryObject<Block> MYCELIATED_FENCE = registerBlock("myceliated_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));

    public static final RegistryObject<Block> MYCELIATED_FENCE_GATE = registerBlock("myceliated_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));

    public static final RegistryObject<Block> MYCELIATED_DOOR = registerBlock("myceliated_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> MYCELIATED_TRAPDOOR = registerBlock("myceliated_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> MYCELIATED_BUTTON = registerBlock("myceliated_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));

    public static final RegistryObject<Block> MYCELIATED_PRESSURE_PLATE = registerBlock("myceliated_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));

    public static final RegistryObject<Block> LIONS_MANE_CLUSTER = registerBlock("lions_mane_cluster",
            () -> new LionsManeMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> BLUE_ANGEL_CLUSTER = registerBlock("blue_angel_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> CHANTERELLE_CLUSTER = registerBlock("chanterelle_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> RUSSULA_CLUSTER = registerBlock("russula_cluster",
            () -> new RussulaClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> RUSSULA_BLOCK = registerBlockWithoutItem("russula_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> CHANTERELLE_BLOCK = registerBlockWithoutItem("chanterelle_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> OYSTER_CLUSTER = registerBlock("oyster_cluster",
            () -> new LogMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> OYSTER_BLOCK = registerBlockWithoutItem("oyster_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> SHIITAKE_CLUSTER = registerBlock("shiitake_cluster",
            () -> new LogMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> SHIITAKE_BLOCK = registerBlockWithoutItem("shiitake_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> KING_TRUMPET_CLUSTER = registerBlock("king_trumpet_cluster",
            () -> new LogMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> KING_TRUMPET_BLOCK = registerBlockWithoutItem("king_trumpet_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> PORTOBELLO_CLUSTER = registerBlock("portobello_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> PORTOBELLO_BLOCK = registerBlockWithoutItem("portobello_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> MOREL_CLUSTER = registerBlock("morel_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> MOREL_BLOCK = registerBlockWithoutItem("morel_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> FLY_AGARIC_CLUSTER = registerBlock("fly_agaric_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> FLY_AGARIC_BLOCK = registerBlockWithoutItem("fly_agaric_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> DEATH_CAP_CLUSTER = registerBlock("death_cap_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> DEATH_CAP_BLOCK = registerBlockWithoutItem("death_cap_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> FREEDOM_CAP_CLUSTER = registerBlock("freedom_cap_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> FREEDOM_CAP_BLOCK = registerBlockWithoutItem("freedom_cap_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> MAGICAL_MUSHROOM_CLUSTER = registerBlock("magical_mushroom_cluster",
            () -> new BrownMushroomClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noOcclusion()
                    .instabreak()
                    .noCollission()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> MAGICAL_MUSHROOM_BLOCK = registerBlockWithoutItem("magical_mushroom_block",
            () -> new TreeGrowableMushroomBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()));

    public static final RegistryObject<Block> DUNG_LAYER = registerBlock("dung_layer",() -> new DungLayerBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN)
            .strength(0.1F)
            .sound(SoundType.HONEY_BLOCK)
            .replaceable()
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion()
            .isViewBlocking((state, world, pos) -> false)));

    public static final RegistryObject<Block> TURTLE_DUNG_LAYER = registerBlock("turtle_dung_layer",() -> new DungLayerBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN)
            .strength(0.1F)
            .sound(SoundType.HONEY_BLOCK)
            .replaceable()
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion()
            .isViewBlocking((state, world, pos) -> false)));

    public static final RegistryObject<Block> MYCELIATED_SAPLING = registerBlock("myceliated_sapling",
            () -> new SaplingBlock(new MyceliatedTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> DUNG_BLOCK = registerBlock("dung_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.HONEY_BLOCK)));

    public static final RegistryObject<Block> TURTLE_DUNG_BLOCK = registerBlock("turtle_dung_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.HONEY_BLOCK)));


    public static final RegistryObject<Block> MULCH_BLOCK = registerBlock("mulch_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.HONEY_BLOCK)));

    public static final RegistryObject<Block> TURTLE_MULCH_BLOCK = registerBlock("turtle_mulch_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.HONEY_BLOCK)));

    public static final RegistryObject<Block> MYCELIATED_DIRT = registerBlock("myceliated_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.ROOTED_DIRT)));

    public static final RegistryObject<Block> MUSHROOM_DRYING_STATION = registerBlock("mushroom_drying_station",
            () -> new MushroomDryingStation(BlockBehaviour.Properties.copy(Blocks.STONECUTTER).noOcclusion()));

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
}
