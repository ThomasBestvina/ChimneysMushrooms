package net.chimney.mushrooms.config;

import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue PSYCHEDELIC_AMPLIFIER_OFFSET;
    public static final ForgeConfigSpec.DoubleValue PSYCHEDELIC_XP_BASE_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue PSYCHEDELIC_XP_PER_AMPLIFIER;

    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_EFFECT_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_ITEM_REGEN_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_ITEM_REGEN_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_REGEN_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_REGEN_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_ABSORPTION_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_ABSORPTION_AMPLIFIER;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_FIRE_RES_DURATION_TICKS;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_TOTEM_FIRE_RES_AMPLIFIER;
    public static final ForgeConfigSpec.DoubleValue BLUE_ANGEL_SURVIVAL_HEALTH;

    public static final ForgeConfigSpec.IntValue DUNG_TRAIL_PLACEMENT_INTERVAL_TICKS;

    public static final ForgeConfigSpec.DoubleValue SPAWN_BAG_GLOBAL_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue BROWN_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue RED_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue RUSSULA_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue LIONS_MANE_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue BLUE_ANGEL_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue CHANTERELLE_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue MAGICAL_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue OYSTER_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue SHIITAKE_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue KING_TRUMPET_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue PORTOBELLO_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue MOREL_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FLY_AGARIC_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue DEATH_CAP_GROWTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FREEDOM_CAP_GROWTH_MULTIPLIER;

    public static final ForgeConfigSpec.IntValue SUBSTRATE_COLONIZATION_INTERVAL;
    public static final ForgeConfigSpec.IntValue MULCH_COLONIZATION_INTERVAL;
    public static final ForgeConfigSpec.IntValue SUBSTRATE_SPREAD_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.IntValue LOG_COLONIZATION_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.IntValue LOG_MUSHROOM_GROWTH_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.IntValue LOG_SPREAD_CHANCE_DENOMINATOR;
    public static final ForgeConfigSpec.IntValue CLUSTER_GROWTH_CHANCE_DENOMINATOR;

    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_END_MIN_GUARANTEED;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_END_MAX_GUARANTEED;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_END_PLACEMENT_RETRIES;
    public static final ForgeConfigSpec.IntValue BLUE_ANGEL_END_FIRST_PASS_ATTEMPTS;
    public static final ForgeConfigSpec.BooleanValue FREEDOM_CAP_SHOW_FLAG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("psychedelic");
        PSYCHEDELIC_AMPLIFIER_OFFSET = builder.comment("Additional amplifier offset applied before XP scaling.")
                .defineInRange("amplifierOffset", 2, 0, 10);
        PSYCHEDELIC_XP_BASE_MULTIPLIER = builder.comment("Base multiplier for XP gained while Psychedelic is active.")
                .defineInRange("xpBaseMultiplier", 1.0d, 0.0d, 100.0d);
        PSYCHEDELIC_XP_PER_AMPLIFIER = builder.comment("Extra XP multiplier per effective amplifier level.")
                .defineInRange("xpPerAmplifier", 0.5d, 0.0d, 100.0d);
        builder.pop();

        builder.push("blueAngel");
        BLUE_ANGEL_EFFECT_DURATION_TICKS = builder.comment("Blue Angel effect duration in ticks.")
                .defineInRange("effectDurationTicks", 90, 1, 72000);
        BLUE_ANGEL_ITEM_REGEN_DURATION_TICKS = builder.defineInRange("itemRegenDurationTicks", 90, 1, 72000);
        BLUE_ANGEL_ITEM_REGEN_AMPLIFIER = builder.defineInRange("itemRegenAmplifier", 4, 0, 10);
        BLUE_ANGEL_TOTEM_REGEN_DURATION_TICKS = builder.defineInRange("totemRegenDurationTicks", 900, 1, 72000);
        BLUE_ANGEL_TOTEM_REGEN_AMPLIFIER = builder.defineInRange("totemRegenAmplifier", 1, 0, 10);
        BLUE_ANGEL_TOTEM_ABSORPTION_DURATION_TICKS = builder.defineInRange("totemAbsorptionDurationTicks", 100, 1, 72000);
        BLUE_ANGEL_TOTEM_ABSORPTION_AMPLIFIER = builder.defineInRange("totemAbsorptionAmplifier", 1, 0, 10);
        BLUE_ANGEL_TOTEM_FIRE_RES_DURATION_TICKS = builder.defineInRange("totemFireResDurationTicks", 800, 1, 72000);
        BLUE_ANGEL_TOTEM_FIRE_RES_AMPLIFIER = builder.defineInRange("totemFireResAmplifier", 0, 0, 10);
        BLUE_ANGEL_SURVIVAL_HEALTH = builder.comment("Health set after Blue Angel death prevention triggers.")
                .defineInRange("survivalHealth", 1.0d, 0.1d, 1024.0d);
        builder.pop();

        builder.push("dungTrail");
        DUNG_TRAIL_PLACEMENT_INTERVAL_TICKS = builder.comment("How often entities with Dung Trail place dung layers.")
                .defineInRange("placementIntervalTicks", 4, 1, 200);
        builder.pop();

        builder.push("spawnBagGrowth");
        SPAWN_BAG_GLOBAL_GROWTH_MULTIPLIER = builder.comment("Global multiplier for spawn bag colonization time.")
                .defineInRange("globalMultiplier", 1.0d, 0.01d, 100.0d);
        BROWN_GROWTH_MULTIPLIER = builder.defineInRange("brownMultiplier", 1.0d, 0.01d, 100.0d);
        RED_GROWTH_MULTIPLIER = builder.defineInRange("redMultiplier", 1.0d, 0.01d, 100.0d);
        RUSSULA_GROWTH_MULTIPLIER = builder.defineInRange("russulaMultiplier", 1.0d, 0.01d, 100.0d);
        LIONS_MANE_GROWTH_MULTIPLIER = builder.defineInRange("lionsManeMultiplier", 1.0d, 0.01d, 100.0d);
        BLUE_ANGEL_GROWTH_MULTIPLIER = builder.defineInRange("blueAngelMultiplier", 1.0d, 0.01d, 100.0d);
        CHANTERELLE_GROWTH_MULTIPLIER = builder.defineInRange("chanterelleMultiplier", 1.0d, 0.01d, 100.0d);
        MAGICAL_GROWTH_MULTIPLIER = builder.defineInRange("magicalMushroomMultiplier", 1.0d, 0.01d, 100.0d);
        OYSTER_GROWTH_MULTIPLIER = builder.defineInRange("oysterMultiplier", 1.0d, 0.01d, 100.0d);
        SHIITAKE_GROWTH_MULTIPLIER = builder.defineInRange("shiitakeMultiplier", 1.0d, 0.01d, 100.0d);
        KING_TRUMPET_GROWTH_MULTIPLIER = builder.defineInRange("kingTrumpetMultiplier", 1.0d, 0.01d, 100.0d);
        PORTOBELLO_GROWTH_MULTIPLIER = builder.defineInRange("portobelloMultiplier", 1.0d, 0.01d, 100.0d);
        MOREL_GROWTH_MULTIPLIER = builder.defineInRange("morelMultiplier", 1.0d, 0.01d, 100.0d);
        FLY_AGARIC_GROWTH_MULTIPLIER = builder.defineInRange("flyAgaricMultiplier", 1.0d, 0.01d, 100.0d);
        DEATH_CAP_GROWTH_MULTIPLIER = builder.defineInRange("deathCapMultiplier", 1.0d, 0.01d, 100.0d);
        FREEDOM_CAP_GROWTH_MULTIPLIER = builder.defineInRange("freedomCapMultiplier", 1.0d, 0.01d, 100.0d);
        builder.pop();

        builder.push("inoculation");
        SUBSTRATE_COLONIZATION_INTERVAL = builder.defineInRange("substrateColonizationInterval", 12, 1, 1000);
        MULCH_COLONIZATION_INTERVAL = builder.defineInRange("mulchColonizationInterval", 4, 1, 1000);
        SUBSTRATE_SPREAD_CHANCE_DENOMINATOR = builder.comment("1 in N chance each random tick.")
                .defineInRange("substrateSpreadChanceDenominator", 5, 1, 10000);
        LOG_COLONIZATION_CHANCE_DENOMINATOR = builder.defineInRange("logColonizationChanceDenominator", 10, 1, 10000);
        LOG_MUSHROOM_GROWTH_CHANCE_DENOMINATOR = builder.defineInRange("logMushroomGrowthChanceDenominator", 25, 1, 10000);
        LOG_SPREAD_CHANCE_DENOMINATOR = builder.defineInRange("logSpreadChanceDenominator", 50, 1, 10000);
        CLUSTER_GROWTH_CHANCE_DENOMINATOR = builder.defineInRange("clusterGrowthChanceDenominator", 8, 1, 10000);
        builder.pop();

        builder.push("blueAngelEndGeneration");
        BLUE_ANGEL_END_MIN_GUARANTEED = builder.defineInRange("minGuaranteedMainIsland", 1, 0, 64);
        BLUE_ANGEL_END_MAX_GUARANTEED = builder.defineInRange("maxGuaranteedMainIsland", 2, 0, 64);
        BLUE_ANGEL_END_PLACEMENT_RETRIES = builder.defineInRange("fallbackRetries", 24, 0, 10000);
        BLUE_ANGEL_END_FIRST_PASS_ATTEMPTS = builder.defineInRange("firstPassAttemptsPerCluster", 96, 1, 10000);
        builder.pop();

        builder.push("freedomCap");
        FREEDOM_CAP_SHOW_FLAG = builder.comment("If false, disables the American flag overlay in the Freedom Cap shader effect.")
                .define("showAmericanFlagOverlay", true);
        builder.pop();

        SPEC = builder.build();
    }

    public static int getSpawnBagGrowthTime(MushroomType type, int defaultTicks) {
        double multiplier = SPAWN_BAG_GLOBAL_GROWTH_MULTIPLIER.get() * getMushroomGrowthMultiplier(type);
        return Math.max(1, (int) Math.round(defaultTicks * multiplier));
    }

    private static double getMushroomGrowthMultiplier(MushroomType type) {
        return switch (type) {
            case BROWN -> BROWN_GROWTH_MULTIPLIER.get();
            case RED -> RED_GROWTH_MULTIPLIER.get();
            case RUSSULA -> RUSSULA_GROWTH_MULTIPLIER.get();
            case LIONS_MANE -> LIONS_MANE_GROWTH_MULTIPLIER.get();
            case BLUE_ANGEL -> BLUE_ANGEL_GROWTH_MULTIPLIER.get();
            case CHANTERELLE -> CHANTERELLE_GROWTH_MULTIPLIER.get();
            case MAGICAL_MUSHROOM -> MAGICAL_GROWTH_MULTIPLIER.get();
            case OYSTER -> OYSTER_GROWTH_MULTIPLIER.get();
            case SHIITAKE -> SHIITAKE_GROWTH_MULTIPLIER.get();
            case KING_TRUMPET -> KING_TRUMPET_GROWTH_MULTIPLIER.get();
            case PORTOBELLO -> PORTOBELLO_GROWTH_MULTIPLIER.get();
            case MOREL -> MOREL_GROWTH_MULTIPLIER.get();
            case FLY_AGARIC -> FLY_AGARIC_GROWTH_MULTIPLIER.get();
            case DEATH_CAP -> DEATH_CAP_GROWTH_MULTIPLIER.get();
            case FREEDOM_CAP -> FREEDOM_CAP_GROWTH_MULTIPLIER.get();
        };
    }
}
