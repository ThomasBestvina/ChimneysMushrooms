package net.chimney.mushrooms.datagen.loot;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.custom.LogMushroomClusterBlock;
import net.chimney.mushrooms.block.custom.MushroomClusterBlock;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.INOCULATED_SUBSTRATE.get(),
                block -> createSingleItemTable(ModBlocks.MYCELIATED_DIRT.get()));

        this.dropSelf(ModBlocks.MYCELIATED_PLANK.get());

        this.dropSelf(ModBlocks.MUSHROOM_DRYING_STATION.get());
        this.dropSelf(ModBlocks.MYCELIATED_SAPLING.get());

        this.dropSelf(ModBlocks.MYCELIATED_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_MYCELIATED_LOG.get());
        this.dropSelf(ModBlocks.MYCELIATED_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_MYCELIATED_WOOD.get());
        this.dropSelf(ModBlocks.MYCELIATED_STAIRS.get());
        this.add(ModBlocks.MYCELIATED_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.MYCELIATED_FENCE.get());
        this.dropSelf(ModBlocks.MYCELIATED_FENCE_GATE.get());
        this.add(ModBlocks.MYCELIATED_DOOR.get(), this::createDoorTable);
        this.dropSelf(ModBlocks.MYCELIATED_TRAPDOOR.get());
        this.dropSelf(ModBlocks.MYCELIATED_BUTTON.get());
        this.dropSelf(ModBlocks.MYCELIATED_PRESSURE_PLATE.get());
        this.add(ModBlocks.INOCULATED_MULCH.get(), block -> createSingleItemTable(ModBlocks.MYCELIATED_DIRT.get()));

        this.add(ModBlocks.INOCULATED_TURTLE_MULCH.get(), block -> createSingleItemTable(ModBlocks.MYCELIATED_DIRT.get()));

        this.add(ModBlocks.RUSSULA_BLOCK.get(),
                block -> createSingleItemTable(ModItems.RUSSULA.get()));

        this.add(ModBlocks.CHANTERELLE_BLOCK.get(),
                block -> createSingleItemTable(ModItems.CHANTERELLE.get()));

        this.add(ModBlocks.MAGICAL_MUSHROOM_BLOCK.get(),
                block -> createSingleItemTable(ModItems.MAGICAL_MUSHROOM.get()));

        this.add(ModBlocks.OYSTER_BLOCK.get(),
                block -> createSingleItemTable(ModItems.OYSTER.get()));

        this.add(ModBlocks.SHIITAKE_BLOCK.get(),
                block -> createSingleItemTable(ModItems.SHIITAKE.get()));

        this.add(ModBlocks.KING_TRUMPET_BLOCK.get(),
                block -> createSingleItemTable(ModItems.KING_TRUMPET.get()));

        this.add(ModBlocks.PORTOBELLO_BLOCK.get(),
                block -> createSingleItemTable(ModItems.PORTOBELLO.get()));

        this.add(ModBlocks.MOREL_BLOCK.get(),
                block -> createSingleItemTable(ModItems.MOREL.get()));

        this.add(ModBlocks.FLY_AGARIC_BLOCK.get(),
                block -> createSingleItemTable(ModItems.FLY_AGARIC.get()));

        this.add(ModBlocks.DEATH_CAP_BLOCK.get(),
                block -> createSingleItemTable(ModItems.DEATH_CAP.get()));

        this.add(ModBlocks.FREEDOM_CAP_BLOCK.get(),
                block -> createSingleItemTable(ModItems.FREEDOM_CAP.get()));


        this.add(ModBlocks.BLUE_ANGEL_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.BLUE_ANGEL.get(), 1.0f, 1.0f));

        this.dropSelf(ModBlocks.MULCH_BLOCK.get());
        this.add(ModBlocks.DUNG_BLOCK.get(),
                block -> createSilkTouchDispatchTable(block,
                        applyExplosionDecay(block, LootItem.lootTableItem(ModItems.DUNG.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0f))))));
        this.add(ModBlocks.TURTLE_DUNG_BLOCK.get(),
                block -> createSilkTouchDispatchTable(block,
                        applyExplosionDecay(block, LootItem.lootTableItem(ModItems.TURTLE_DUNG.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0f))))));
        this.dropSelf(ModBlocks.TURTLE_MULCH_BLOCK.get());
        this.dropSelf(ModBlocks.MYCELIATED_DIRT.get());

        this.add(ModBlocks.BROWN_MUSHROOM_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, Items.BROWN_MUSHROOM, 5.0f, 8.0f));

        this.add(ModBlocks.DUNG_LAYER.get(), block -> createDungLayerLoot(block, ModItems.DUNG.get()));
        this.add(ModBlocks.TURTLE_DUNG_LAYER.get(), block -> createDungLayerLoot(block, ModItems.TURTLE_DUNG.get()));

        this.add(ModBlocks.RED_MUSHROOM_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, Items.RED_MUSHROOM, 5.0f, 8.0f));

        this.add(ModBlocks.RUSSULA_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.RUSSULA.get(), 5.0f, 8.0f));

        this.add(ModBlocks.CHANTERELLE_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.CHANTERELLE.get(), 5.0f, 8.0f));

        this.add(ModBlocks.MAGICAL_MUSHROOM_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.MAGICAL_MUSHROOM.get(), 5.0f, 8.0f));

        this.add(ModBlocks.OYSTER_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.OYSTER.get(), 3.0f, 6.0f));

        this.add(ModBlocks.SHIITAKE_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.SHIITAKE.get(), 3.0f, 6.0f));

        this.add(ModBlocks.KING_TRUMPET_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.KING_TRUMPET.get(), 3.0f, 6.0f));

        this.add(ModBlocks.PORTOBELLO_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.PORTOBELLO.get(), 5.0f, 8.0f));

        this.add(ModBlocks.MOREL_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.MOREL.get(), 5.0f, 8.0f));

        this.add(ModBlocks.FLY_AGARIC_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.FLY_AGARIC.get(), 5.0f, 8.0f));

        this.add(ModBlocks.DEATH_CAP_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.DEATH_CAP.get(), 1.0f, 3.0f));

        this.add(ModBlocks.FREEDOM_CAP_CLUSTER.get(),
                block -> createClusterLootWithSilkOrShears(block, ModItems.FREEDOM_CAP.get(), 5.0f, 8.0f));


        this.add(ModBlocks.LIONS_MANE_CLUSTER.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(block)
                                                .when(hasSilkTouchOrShears())
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                .hasProperty(LogMushroomClusterBlock.GROWTH_STAGE, 3))),
                                        LootItem.lootTableItem(ModItems.LIONS_MANE.get())
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                .hasProperty(LogMushroomClusterBlock.GROWTH_STAGE, 3)))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F)))
                                                .setWeight(10),
                                        LootItem.lootTableItem(Items.STICK)
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                .hasProperty(LogMushroomClusterBlock.GROWTH_STAGE, 3)))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                .setWeight(1)
                                ))
                        )
        );

        this.add(ModBlocks.INOCULATED_LOG.get(),
                block -> createSingleItemTable(ModBlocks.MYCELIATED_LOG.get()));
    }

    private LootTable.Builder createClusterLootWithSilkOrShears(Block clusterBlock, Item matureDrop, float minCount, float maxCount) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(clusterBlock)
                                        .when(hasSilkTouchOrShears())
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(clusterBlock)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(MushroomClusterBlock.GROWTH_STAGE, 3))),
                                LootItem.lootTableItem(matureDrop)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(clusterBlock)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(MushroomClusterBlock.GROWTH_STAGE, 3)))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)))
                        )));
    }

    private LootTable.Builder createDungLayerLoot(Block layerBlock, Item dungItem) {
        LootPool.Builder poolBuilder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F));
        for (int layers = 8; layers >= 1; layers--) {
            poolBuilder.add(
                    LootItem.lootTableItem(dungItem)
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(layerBlock)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(SnowLayerBlock.LAYERS, layers)))
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers)))
            );
        }
        return LootTable.lootTable().withPool(poolBuilder);
    }

    private AnyOfCondition.Builder hasSilkTouchOrShears() {
        return AnyOfCondition.anyOf(
                hasSilkTouchCondition(),
                MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))
        );
    }

    private MatchTool.Builder hasSilkTouchCondition() {
        return MatchTool.toolMatches(ItemPredicate.Builder.item()
                .hasEnchantment(new EnchantmentPredicate(
                        Enchantments.SILK_TOUCH,
                        MinMaxBounds.Ints.atLeast(1)
                )));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }
}
