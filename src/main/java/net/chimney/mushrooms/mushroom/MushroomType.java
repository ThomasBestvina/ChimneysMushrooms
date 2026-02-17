package net.chimney.mushrooms.mushroom;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum MushroomType {
    BROWN("brown",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Items.BROWN_MUSHROOM,
            4000, // 3.3 minutes
            0.65f, 0.38f, 0.25f,
            (level, pos) -> Blocks.BROWN_MUSHROOM.defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    RUSSULA("russala",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            ModItems.RUSSULA.get(),
            4000, // 3.3 minutes
            0.65f, 0.38f, 0.25f,
            (level, pos) -> ModBlocks.RUSSULA_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),


    RED("red",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Items.RED_MUSHROOM,
            4000,
            0.9f, 0.2f, 0.2f,
            (level, pos) -> Blocks.RED_MUSHROOM.defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),


    LIONS_MANE("lions_mane",
            Blocks.OAK_LOG,
            Blocks.STRIPPED_OAK_LOG,
            ModItems.LIONS_MANE.get(),
            7000,
            0.95f, 0.85f, 0.75f,
            (level, pos) -> {
                return Blocks.AIR.defaultBlockState();
            },
            SoundEvents.WOOD_PLACE,
            (level, pos, state) -> {
                Block below = level.getBlockState(pos.below()).getBlock();
                return MushroomType.isAnyWoodBlock(below);
            }),

    BLUE_ANGEL("blue_angel",
            null, // Will be set in common setup to avoid early access
            null,
            ModItems.BLUE_ANGEL.get(),
            7000,
            0.0f, 0.15f, 0.75f,
            (level, pos) -> {
                return ModBlocks.BLUE_ANGEL_CLUSTER.get().defaultBlockState();
            },
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    CHANTERELLE("chanterelle",
            null, // Will be set to DUNG_BLOCK in common setup
            null,
            ModItems.CHANTERELLE.get(),
            5500,
            1.0f, 0.6f, 0.0f,
            (level, pos) -> ModBlocks.CHANTERELLE_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    MAGICAL_MUSHROOM("magical_mushroom",
            null, // Will be set to DUNG_BLOCK in common setup
            null,
            ModItems.MAGICAL_MUSHROOM.get(),
            4500,
            0.8f, 0.0f, 1.0f,
            (level, pos) -> ModBlocks.MAGICAL_MUSHROOM_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    OYSTER("oyster",
            Blocks.OAK_LOG,
            Blocks.STRIPPED_OAK_LOG,
            ModItems.OYSTER.get(),
            5000,
            0.9f, 0.9f, 0.9f,
            (level, pos) -> Blocks.AIR.defaultBlockState(),
            SoundEvents.WOOD_PLACE,
            (level, pos, state) -> {
                Block below = level.getBlockState(pos.below()).getBlock();
                return MushroomType.isAnyWoodBlock(below);
            }),

    SHIITAKE("shiitake",
            Blocks.OAK_LOG,
            Blocks.STRIPPED_OAK_LOG,
            ModItems.SHIITAKE.get(),
            6000,
            0.5f, 0.3f, 0.2f,
            (level, pos) -> Blocks.AIR.defaultBlockState(),
            SoundEvents.WOOD_PLACE,
            (level, pos, state) -> {
                Block below = level.getBlockState(pos.below()).getBlock();
                return MushroomType.isAnyWoodBlock(below);
            }),

    KING_TRUMPET("king_trumpet",
            Blocks.OAK_LOG,
            Blocks.STRIPPED_OAK_LOG,
            ModItems.KING_TRUMPET.get(),
            6500,
            0.8f, 0.7f, 0.6f,
            (level, pos) -> Blocks.AIR.defaultBlockState(),
            SoundEvents.WOOD_PLACE,
            (level, pos, state) -> {
                Block below = level.getBlockState(pos.below()).getBlock();
                return MushroomType.isAnyWoodBlock(below);
            }),

    PORTOBELLO("portobello",
            null, // Will be set to DUNG_BLOCK
            null,
            ModItems.PORTOBELLO.get(),
            4800,
            0.6f, 0.4f, 0.3f,
            (level, pos) -> ModBlocks.PORTOBELLO_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    MOREL("morel",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            ModItems.MOREL.get(),
            5500,
            0.7f, 0.6f, 0.4f,
            (level, pos) -> ModBlocks.MOREL_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    FLY_AGARIC("fly_agaric",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            ModItems.FLY_AGARIC.get(),
            4500,
            1.0f, 0.1f, 0.1f,
            (level, pos) -> ModBlocks.FLY_AGARIC_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    DEATH_CAP("death_cap",
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            ModItems.DEATH_CAP.get(),
            6000,
            0.8f, 1.0f, 0.6f,
            (level, pos) -> ModBlocks.DEATH_CAP_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos)),

    FREEDOM_CAP("freedom_cap",
            Blocks.GRASS_BLOCK,
            Blocks.DIRT,
            ModItems.FREEDOM_CAP.get(),
            5000,
            0.6f, 0.5f, 0.3f,
            (level, pos) -> ModBlocks.FREEDOM_CAP_CLUSTER.get().defaultBlockState(),
            SoundEvents.GRASS_PLACE,
            (level, pos, state) -> state.canSurvive(level, pos));


    private final String name;
    private Block primaryBlock;
    private final Block secondaryBlock;
    private final Item resultItem;
    private final int growthTime;
    private final float red, green, blue;
    private final BiFunction<Level, BlockPos, BlockState> plantingBehavior;
    private final SoundEvent plantingSound;
    private final TriFunction<Level, BlockPos, BlockState, Boolean> canSurviveCheck;

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    MushroomType(String name, Block primaryBlock, Block secondaryBlock,
                 Item resultItem, int growthTime, float red, float green, float blue,
                 BiFunction<Level, BlockPos, BlockState> plantingBehavior,
                 SoundEvent plantingSound,
                 TriFunction<Level, BlockPos, BlockState, Boolean> canSurviveCheck) {
        this.name = name;
        this.primaryBlock = primaryBlock;
        this.secondaryBlock = secondaryBlock;
        this.resultItem = resultItem;
        this.growthTime = growthTime;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.plantingBehavior = plantingBehavior;
        this.plantingSound = plantingSound;
        this.canSurviveCheck = canSurviveCheck;
    }

    public void setPrimaryBlock(Block block) { this.primaryBlock = block; }

    public String getName() { return name; }
    public Block getPrimaryBlock() { return primaryBlock; }
    public Block getSecondaryBlock() { return secondaryBlock; }
    public Item getResultItem() { return resultItem; }
    public int getGrowthTime() { return growthTime; }
    public float getRed() { return red; }
    public float getGreen() { return green; }
    public float getBlue() { return blue; }
    public SoundEvent getPlantingSound() { return plantingSound; }

    public boolean canPlantOn(Block block) {
        if(isLogBased())
        {
            return isWoodBlock(block);
        }
        return block == primaryBlock ||
                (secondaryBlock != null && block == secondaryBlock);
    }

    public static boolean isWoodBlock(Block block)
    {
        BlockState state = block.defaultBlockState();
        return state.is(BlockTags.LOGS) || isExplicitStrippedWood(block);
    }

    public static boolean isAnyWoodBlock(Block block)
    {
        return isWoodBlock(block);
    }

    private static boolean isExplicitStrippedWood(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        if (key == null) {
            return false;
        }

        String path = key.getPath();
        if (!path.startsWith("stripped_")) {
            return false;
        }

        return path.endsWith("_log")
                || path.endsWith("_wood")
                || path.endsWith("_stem")
                || path.endsWith("_hyphae");
    }


    public boolean isLogBased()
    {
        return this.primaryBlock == Blocks.OAK_LOG;
    }

    public BlockState getPlantingState(Level level, BlockPos pos) {
        return plantingBehavior.apply(level, pos);
    }

    public boolean canSurviveAt(Level level, BlockPos pos, BlockState state) {
        return canSurviveCheck.apply(level, pos, state);
    }

    public float getGrowthModifier(Level level, BlockPos pos) {
        float modifier = 1.0f;

        if (this == BROWN || this == RED) {
            if (level.getMaxLocalRawBrightness(pos) < 7) {
                modifier += 0.5f;
            }
        }

        return modifier;
    }

    public ParticleOptions getPlantingParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    public String getGrowthHint() {
        return switch (this) {
            case BROWN, RED -> "tooltip.chimneysmushrooms.grows_in_dark";
            default -> "";
        };
    }

    public boolean canObtainFromBlock(Block block) {
        return switch (this) {
            case BROWN -> block == Blocks.BROWN_MUSHROOM || block == Blocks.BROWN_MUSHROOM_BLOCK || block == ModBlocks.BROWN_MUSHROOM_CLUSTER.get();
            case RED -> block == Blocks.RED_MUSHROOM || block == Blocks.RED_MUSHROOM_BLOCK || block == ModBlocks.RED_MUSHROOM_CLUSTER.get();
            case LIONS_MANE -> block == ModBlocks.LIONS_MANE_CLUSTER.get();
            case RUSSULA -> block == ModBlocks.RUSSULA_CLUSTER.get() || block == ModBlocks.RUSSULA_BLOCK.get();
            case BLUE_ANGEL -> block == ModBlocks.BLUE_ANGEL_CLUSTER.get();
            case CHANTERELLE -> block == ModBlocks.CHANTERELLE_CLUSTER.get();
            case MAGICAL_MUSHROOM -> block == ModBlocks.MAGICAL_MUSHROOM_CLUSTER.get() || block == ModBlocks.MAGICAL_MUSHROOM_BLOCK.get();
            case OYSTER -> block == ModBlocks.OYSTER_CLUSTER.get() || block == ModBlocks.OYSTER_BLOCK.get();
            case SHIITAKE -> block == ModBlocks.SHIITAKE_CLUSTER.get() || block == ModBlocks.SHIITAKE_BLOCK.get();
            case KING_TRUMPET -> block == ModBlocks.KING_TRUMPET_CLUSTER.get() || block == ModBlocks.KING_TRUMPET_BLOCK.get();
            case PORTOBELLO -> block == ModBlocks.PORTOBELLO_CLUSTER.get() || block == ModBlocks.PORTOBELLO_BLOCK.get();
            case MOREL -> block == ModBlocks.MOREL_CLUSTER.get() || block == ModBlocks.MOREL_BLOCK.get();
            case FLY_AGARIC -> block == ModBlocks.FLY_AGARIC_CLUSTER.get() || block == ModBlocks.FLY_AGARIC_BLOCK.get();
            case DEATH_CAP -> block == ModBlocks.DEATH_CAP_CLUSTER.get() || block == ModBlocks.DEATH_CAP_BLOCK.get();
            case FREEDOM_CAP -> block == ModBlocks.FREEDOM_CAP_CLUSTER.get() || block == ModBlocks.FREEDOM_CAP_BLOCK.get();
            default -> false;
        };
    }

    public boolean canObtainFromItem(Item item) {
        return switch (this) {
            case BROWN -> item == Items.BROWN_MUSHROOM;
            case RED -> item == Items.RED_MUSHROOM;
            case LIONS_MANE -> item == ModItems.LIONS_MANE.get();
            case RUSSULA -> item == ModItems.RUSSULA.get();
            case BLUE_ANGEL -> item == ModItems.BLUE_ANGEL.get();
            case CHANTERELLE -> item == ModItems.CHANTERELLE.get();
            case MAGICAL_MUSHROOM -> item == ModItems.MAGICAL_MUSHROOM.get();
            case OYSTER -> item == ModItems.OYSTER.get();
            case SHIITAKE -> item == ModItems.SHIITAKE.get();
            case KING_TRUMPET -> item == ModItems.KING_TRUMPET.get();
            case PORTOBELLO -> item == ModItems.PORTOBELLO.get();
            case MOREL -> item == ModItems.MOREL.get();
            case FLY_AGARIC -> item == ModItems.FLY_AGARIC.get();
            case DEATH_CAP -> item == ModItems.DEATH_CAP.get();
            case FREEDOM_CAP -> item == ModItems.FREEDOM_CAP.get();
            default -> false;
        };
    }
}
