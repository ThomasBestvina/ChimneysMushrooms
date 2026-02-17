package net.chimney.mushrooms.datagen;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChimneysMushrooms.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        this.tag(BlockTags.LOGS)
                .add(ModBlocks.INOCULATED_LOG.get())
                .add(ModBlocks.MYCELIATED_LOG.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .add(ModBlocks.MYCELIATED_WOOD.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_WOOD.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.INOCULATED_LOG.get())
                .add(ModBlocks.MYCELIATED_LOG.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .add(ModBlocks.MYCELIATED_WOOD.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_WOOD.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.MYCELIATED_PLANK.get());

        this.tag(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.MYCELIATED_STAIRS.get());

        this.tag(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.MYCELIATED_SLAB.get());

        this.tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.MYCELIATED_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.MYCELIATED_FENCE_GATE.get());

        this.tag(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.MYCELIATED_DOOR.get());

        this.tag(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.MYCELIATED_TRAPDOOR.get());

        this.tag(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.MYCELIATED_BUTTON.get());

        this.tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.MYCELIATED_PRESSURE_PLATE.get());

        this.tag(BlockTags.DIRT)
                .add(ModBlocks.MYCELIATED_DIRT.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.INOCULATED_LOG.get())
                .add(ModBlocks.MYCELIATED_LOG.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_LOG.get())
                .add(ModBlocks.MYCELIATED_WOOD.get())
                .add(ModBlocks.STRIPPED_MYCELIATED_WOOD.get())
                .add(ModBlocks.MYCELIATED_PLANK.get())
                .add(ModBlocks.MYCELIATED_STAIRS.get())
                .add(ModBlocks.MYCELIATED_SLAB.get())
                .add(ModBlocks.MYCELIATED_FENCE.get())
                .add(ModBlocks.MYCELIATED_FENCE_GATE.get())
                .add(ModBlocks.MYCELIATED_DOOR.get())
                .add(ModBlocks.MYCELIATED_TRAPDOOR.get())
                .add(ModBlocks.MYCELIATED_BUTTON.get())
                .add(ModBlocks.MYCELIATED_PRESSURE_PLATE.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.MYCELIATED_DIRT.get())
                .add(ModBlocks.INOCULATED_SUBSTRATE.get())
                .add(ModBlocks.INOCULATED_MULCH.get())
                .add(ModBlocks.INOCULATED_TURTLE_MULCH.get())
                .add(ModBlocks.DUNG_BLOCK.get())
                .add(ModBlocks.TURTLE_DUNG_BLOCK.get())
                .add(ModBlocks.MULCH_BLOCK.get())
                .add(ModBlocks.TURTLE_MULCH_BLOCK.get());
    }
}
