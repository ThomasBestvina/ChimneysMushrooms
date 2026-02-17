package net.chimney.mushrooms.datagen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public static final TagKey<Item> DRIED_MUSHROOMS = TagKey.create(net.minecraft.core.registries.Registries.ITEM,
            new ResourceLocation(ChimneysMushrooms.MODID, "dried_mushrooms"));


    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ChimneysMushrooms.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        this.tag(Tags.Items.MUSHROOMS)
                .add(Items.BROWN_MUSHROOM)
                .add(Items.RED_MUSHROOM)
                .add(ModItems.DRIED_BROWN_MUSHROOM.get())
                .add(ModItems.DRIED_RED_MUSHROOM.get())
                .add(ModItems.RUSSULA.get())
                .add(ModItems.DRIED_RUSSULA.get())
                .add(ModItems.LIONS_MANE.get())
                .add(ModItems.DRIED_LIONS_MANE.get())
                .add(ModItems.BLUE_ANGEL.get())
                .add(ModItems.DRIED_BLUE_ANGEL.get())
                .add(ModItems.CHANTERELLE.get())
                .add(ModItems.DRIED_CHANTERELLE.get())
                .add(ModItems.MAGICAL_MUSHROOM.get())
                .add(ModItems.DRIED_MAGICAL_MUSHROOM.get())
                .add(ModItems.FLY_AGARIC.get())
                .add(ModItems.DRIED_FLY_AGARIC.get())
                .add(ModItems.DEATH_CAP.get())
                .add(ModItems.DRIED_DEATH_CAP.get())
                .add(ModItems.FREEDOM_CAP.get())
                .add(ModItems.DRIED_FREEDOM_CAP.get())
                .add(ModItems.OYSTER.get())
                .add(ModItems.DRIED_OYSTER.get())
                .add(ModItems.SHIITAKE.get())
                .add(ModItems.DRIED_SHIITAKE.get())
                .add(ModItems.KING_TRUMPET.get())
                .add(ModItems.DRIED_KING_TRUMPET.get())
                .add(ModItems.PORTOBELLO.get())
                .add(ModItems.DRIED_PORTOBELLO.get())
                .add(ModItems.MOREL.get())
                .add(ModItems.DRIED_MOREL.get());

        this.tag(DRIED_MUSHROOMS)
                .add(ModItems.DRIED_BROWN_MUSHROOM.get())
                .add(ModItems.DRIED_RED_MUSHROOM.get())
                .add(ModItems.DRIED_BLUE_ANGEL.get())
                .add(ModItems.DRIED_RUSSULA.get())
                .add(ModItems.DRIED_CHANTERELLE.get())
                .add(ModItems.DRIED_MAGICAL_MUSHROOM.get())
                .add(ModItems.DRIED_LIONS_MANE.get())
                .add(ModItems.DRIED_FLY_AGARIC.get())
                .add(ModItems.DRIED_DEATH_CAP.get())
                .add(ModItems.DRIED_FREEDOM_CAP.get())
                .add(ModItems.DRIED_OYSTER.get())
                .add(ModItems.DRIED_SHIITAKE.get())
                .add(ModItems.DRIED_KING_TRUMPET.get())
                .add(ModItems.DRIED_PORTOBELLO.get())
                .add(ModItems.DRIED_MOREL.get());

        this.tag(ItemTags.LOGS)
                .add(ModBlocks.INOCULATED_LOG.get().asItem())
                .add(ModBlocks.MYCELIATED_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_MYCELIATED_LOG.get().asItem())
                .add(ModBlocks.MYCELIATED_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_MYCELIATED_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.MYCELIATED_PLANK.get().asItem());

        this.copy(net.minecraft.tags.BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.copy(net.minecraft.tags.BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        this.copy(net.minecraft.tags.BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
    }


}
