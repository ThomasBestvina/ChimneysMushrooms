package net.chimney.mushrooms.item;

import net.chimney.mushrooms.ChimneysMushrooms;

import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.effect.ModEffects;
import net.chimney.mushrooms.item.custom.DungProjectileItem;
import net.chimney.mushrooms.item.custom.FoodBlockItem;
import net.chimney.mushrooms.item.custom.GrainBagItem;
import net.chimney.mushrooms.item.custom.SpawnBagItem;
import net.chimney.mushrooms.item.custom.AmalgamationItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChimneysMushrooms.MODID);
    public static final boolean FARMERS_DELIGHT_LOADED = ModList.get().isLoaded("farmersdelight");


    public static final RegistryObject<Item> SPAWN_BAG = ITEMS.register("spawn_bag",
            SpawnBagItem::new);

    public static final RegistryObject<Item> BAG = ITEMS.register("bag",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GRAIN_BAG = ITEMS.register("grain_bag",
            () -> new GrainBagItem(new Item.Properties()));

    public static final RegistryObject<Item> DRIED_BROWN_MUSHROOM = ITEMS.register("dried_brown_mushroom",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .build())));

    public static final RegistryObject<Item> DRIED_RED_MUSHROOM = ITEMS.register("dried_red_mushroom",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LIONS_MANE = ITEMS.register("lions_mane",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationMod(0.2f)
                            .build())));
    public static final RegistryObject<Item> DRIED_LIONS_MANE = ITEMS.register("dried_lions_mane",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .build())));

    public static final RegistryObject<Item> RUSSULA = ITEMS.register("russula",
            () -> new FoodBlockItem(ModBlocks.RUSSULA_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .effect(() -> new MobEffectInstance(ModEffects.DUNG_TRAIL_EFFECT.getHolder().get().get(), 600, 0), 1.0f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 200, 0), 1.0f)
                                    .build())));

    public static final RegistryObject<Item> DRIED_RUSSULA = ITEMS.register("dried_russula",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(ModEffects.DUNG_TRAIL_EFFECT.getHolder().get().get(), 600, 0), 1.0f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 200, 0), 1.0f)
                            .build())));

    public static final RegistryObject<Item> CHANTERELLE = ITEMS.register("chanterelle",
            () -> new FoodBlockItem(ModBlocks.CHANTERELLE_BLOCK.get(),
                    new Item.Properties()));

    public static final RegistryObject<Item> DRIED_CHANTERELLE = ITEMS.register("dried_chanterelle",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DUNG = ITEMS.register("dung", () -> new DungProjectileItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> TURTLE_DUNG = ITEMS.register("turtle_dung", () -> new DungProjectileItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> AMALGAMATION = ITEMS.register("amalgamation",
            () -> new AmalgamationItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(0)
                            .saturationMod(0.0f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MAGICAL_MUSHROOM = ITEMS.register("magical_mushroom",
            () -> new FoodBlockItem(ModBlocks.MAGICAL_MUSHROOM_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .effect(() -> new MobEffectInstance(ModEffects.PSYCHEDELIC_EFFECT.get(), 20000, 1), 1.0f)
                                    .alwaysEat()
                                    .build())));
    public static final RegistryObject<Item> DRIED_MAGICAL_MUSHROOM = ITEMS.register("dried_magical_mushroom",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(ModEffects.PSYCHEDELIC_EFFECT.get(), 16000, 1), 1.0f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> FLY_AGARIC = ITEMS.register("fly_agaric",
            () -> new FoodBlockItem(ModBlocks.FLY_AGARIC_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 200, 0), 0.5f)
                                    .alwaysEat()
                                    .build())));
    public static final RegistryObject<Item> DRIED_FLY_AGARIC = ITEMS.register("dried_fly_agaric",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 200, 0), 0.5f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> DEATH_CAP = ITEMS.register("death_cap",
            () -> new FoodBlockItem(ModBlocks.DEATH_CAP_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 3600, 1), 1.0f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 1200, 1), 1.0f)
                                    .alwaysEat()
                                    .build())));
    public static final RegistryObject<Item> DRIED_DEATH_CAP = ITEMS.register("dried_death_cap",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 3600, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 1200, 1), 1.0f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> FREEDOM_CAP = ITEMS.register("freedom_cap",
            () -> new FoodBlockItem(ModBlocks.FREEDOM_CAP_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .effect(() -> new MobEffectInstance(ModEffects.ENLIGHTENED_EFFECT.get(), 1200, 0), 1.0f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 1200, 0), 1.0f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SPEED, 1200, 1), 1.0f)
                                    .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 300, 0), 1.0f)
                                    .alwaysEat()
                                    .build())));
    public static final RegistryObject<Item> DRIED_FREEDOM_CAP = ITEMS.register("dried_freedom_cap",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(ModEffects.ENLIGHTENED_EFFECT.get(), 900, 0), 1.0f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 900, 0), 1.0f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SPEED, 900, 1), 1.0f)
                            .effect(() -> new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 200, 0), 1.0f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> OYSTER = ITEMS.register("oyster",
            () -> new FoodBlockItem(ModBlocks.OYSTER_BLOCK.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(3)
                                    .saturationMod(0.2f)
                                    .build())));
    public static final RegistryObject<Item> DRIED_OYSTER = ITEMS.register("dried_oyster",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .build())));

    public static final RegistryObject<Item> SHIITAKE = ITEMS.register("shiitake",
            () -> new FoodBlockItem(ModBlocks.SHIITAKE_BLOCK.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> DRIED_SHIITAKE = ITEMS.register("dried_shiitake",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KING_TRUMPET = ITEMS.register("king_trumpet",
            () -> new FoodBlockItem(ModBlocks.KING_TRUMPET_BLOCK.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> DRIED_KING_TRUMPET = ITEMS.register("dried_king_trumpet",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PORTOBELLO = ITEMS.register("portobello",
            () -> new FoodBlockItem(ModBlocks.PORTOBELLO_BLOCK.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> DRIED_PORTOBELLO = ITEMS.register("dried_portobello",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOREL = ITEMS.register("morel",
            () -> new FoodBlockItem(ModBlocks.MOREL_BLOCK.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> DRIED_MOREL = ITEMS.register("dried_morel",
            () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> BLUE_ANGEL = ITEMS.register("blue_angel",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(0)
                            .saturationMod(0.0f)
                            .effect(() -> new MobEffectInstance(
                                    ModEffects.BLUE_ANGEL_EFFECT.get(),
                                    90,
                                    0,
                                    true,
                                    true,
                                    true
                            ), 1.0f) // Probability (1.0 = 100%)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 90, 4), 1.0f)
                            .alwaysEat() // Can eat even when not hungry
                            .build())));
    public static final RegistryObject<Item> DRIED_BLUE_ANGEL = ITEMS.register("dried_blue_angel",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.15f)
                            .effect(() -> new MobEffectInstance(
                                    ModEffects.BLUE_ANGEL_EFFECT.get(),
                                    70,
                                    0,
                                    true,
                                    true,
                                    true
                            ), 1.0f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 70, 3), 1.0f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> RAW_BEEF_WELLINGTON = ITEMS.register("raw_beef_wellington",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationMod(0.3f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> BEEF_WELLINGTON = ITEMS.register("beef_wellington",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(9)
                            .saturationMod(1.2f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MUSHROOM_SKEWER = ITEMS.register("mushroom_skewer",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(7)
                            .saturationMod(0.5f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> RAW_MUSHROOM_SKEWER = ITEMS.register("raw_mushroom_skewer",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.4f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MUSHROOM_JERKY = ITEMS.register("mushroom_jerky",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(5)
                            .saturationMod(0.3f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> RUST_MUSHROOM_STEW = ITEMS.register("rustic_mushroom_stew",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(9)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())));

    public static final RegistryObject<Item> MUSHROOM_RISOTTO = FARMERS_DELIGHT_LOADED
            ? ITEMS.register("mushroom_risotto",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationMod(0.8f)
                            .alwaysEat()
                            .build())))
            : null;

    public static final RegistryObject<Item> CREAMY_MUSHROOM_PASTA = FARMERS_DELIGHT_LOADED
            ? ITEMS.register("creamy_mushroom_pasta",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(10)
                            .saturationMod(0.9f)
                            .alwaysEat()
                            .build())))
            : null;

    public static final RegistryObject<Item> STUFFED_MUSHROOMS = FARMERS_DELIGHT_LOADED
            ? ITEMS.register("stuffed_mushrooms",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(7)
                            .saturationMod(0.7f)
                            .alwaysEat()
                            .build())))
            : null;



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
