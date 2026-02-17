package net.chimney.mushrooms;

import com.mojang.logging.LogUtils;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.block.entity.ModBlockEntities;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.effect.ModEffects;
import net.chimney.mushrooms.entity.ModEntities;
import net.chimney.mushrooms.item.ModCreativeModTabs;
import net.chimney.mushrooms.item.ModItems;
import net.chimney.mushrooms.potion.ModPotions;
import net.chimney.mushrooms.recipe.DryingRecipe;
import net.chimney.mushrooms.recipe.ModRecipeSerializers;
import net.chimney.mushrooms.recipe.ModRecipeTypes;
import net.chimney.mushrooms.villager.ModVillagers;
import net.chimney.mushrooms.worldgen.ModFeatures;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import java.util.List;

@Mod(ChimneysMushrooms.MODID)
public class ChimneysMushrooms
{
    public static final String MODID = "chimneysmushrooms";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChimneysMushrooms()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC);

        ModFeatures.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModVillagers.register(modEventBus);

        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);

        ModCreativeModTabs.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModBlockEntities.init();

        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(ModItems.DRIED_RUSSULA.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DIARRHEA_POTION.get())
            );
            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(ModItems.DRIED_DEATH_CAP.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.WITHERING_POTION.get())
            );

            net.chimney.mushrooms.mushroom.MushroomType.BLUE_ANGEL.setPrimaryBlock(ModBlocks.TURTLE_MULCH_BLOCK.get());
            net.chimney.mushrooms.mushroom.MushroomType.CHANTERELLE.setPrimaryBlock(ModBlocks.DUNG_BLOCK.get());
            net.chimney.mushrooms.mushroom.MushroomType.MAGICAL_MUSHROOM.setPrimaryBlock(ModBlocks.DUNG_BLOCK.get());
            net.chimney.mushrooms.mushroom.MushroomType.PORTOBELLO.setPrimaryBlock(ModBlocks.DUNG_BLOCK.get());
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        event.getServer().getCommands().getDispatcher().register(
                Commands.literal("checkdrying")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            RecipeManager recipes = player.level().getRecipeManager();

                            List<DryingRecipe> dryingRecipes = recipes.getAllRecipesFor(ModRecipeTypes.DRYING.get());
                            player.sendSystemMessage(Component.literal("=== DRYING RECIPES ==="));
                            player.sendSystemMessage(Component.literal("Count: " + dryingRecipes.size()));

                            for (DryingRecipe recipe : dryingRecipes) {
                                ItemStack[] ingredients = recipe.getIngredient().getItems();
                                ItemStack result = recipe.getResultItem(player.level().registryAccess());

                                if (ingredients.length > 0) {
                                    player.sendSystemMessage(Component.literal(
                                            "  " + ingredients[0].getItem() +
                                                    " -> " + result.getItem() +
                                                    " (" + recipe.getDryingTime() + " ticks)"
                                    ));
                                }
                            }

                            return 1;
                        })
        );
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {});
        }
    }

}
