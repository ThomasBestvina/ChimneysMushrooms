package net.chimney.mushrooms.item;


import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChimneysMushrooms.MODID);

    public static final RegistryObject<CreativeModeTab> CHIMNEYS_MUSHROOMS = CREATIVE_MODE_TABS.register("chimneysmushrooms", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.MAGICAL_MUSHROOM.get()))
            .title(Component.translatable("creativetab.chimneysmushrooms"))
            .displayItems((pParameters, pOutput) -> {
                // Spawn bags
                pOutput.accept(new ItemStack(ModItems.BAG.get(), 1));
                pOutput.accept(new ItemStack(ModItems.GRAIN_BAG.get(), 1));
                pOutput.accept(new ItemStack(ModItems.SPAWN_BAG.get(), 1));
                for (MushroomType type : MushroomType.values()) {
                    ItemStack readyBag = new ItemStack(ModItems.SPAWN_BAG.get());
                    net.minecraft.nbt.CompoundTag tag = readyBag.getOrCreateTag();
                    tag.putString("MushroomType", type.name());
                    tag.putBoolean("Ready", true);
                    tag.putFloat("GrowthPercent", 1.0f);
                    pOutput.accept(readyBag);
                }

                // Mushroom clusters
                pOutput.accept(new ItemStack(ModBlocks.BROWN_MUSHROOM_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.RED_MUSHROOM_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.BLUE_ANGEL_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.RUSSULA_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.CHANTERELLE_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.MAGICAL_MUSHROOM_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.FLY_AGARIC_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.DEATH_CAP_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.FREEDOM_CAP_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.OYSTER_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.SHIITAKE_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.KING_TRUMPET_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.PORTOBELLO_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.MOREL_CLUSTER.get()));
                pOutput.accept(new ItemStack(ModBlocks.LIONS_MANE_CLUSTER.get()));

                // Mushrooms
                pOutput.accept(new ItemStack(ModItems.BLUE_ANGEL.get(), 1));
                pOutput.accept(new ItemStack(ModItems.RUSSULA.get(), 1));
                pOutput.accept(new ItemStack(ModItems.CHANTERELLE.get(), 1));
                pOutput.accept(new ItemStack(ModItems.MAGICAL_MUSHROOM.get(), 1));
                pOutput.accept(new ItemStack(ModItems.FLY_AGARIC.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DEATH_CAP.get(), 1));
                pOutput.accept(new ItemStack(ModItems.FREEDOM_CAP.get(), 1));
                pOutput.accept(new ItemStack(ModItems.OYSTER.get(), 1));
                pOutput.accept(new ItemStack(ModItems.SHIITAKE.get(), 1));
                pOutput.accept(new ItemStack(ModItems.KING_TRUMPET.get(), 1));
                pOutput.accept(new ItemStack(ModItems.PORTOBELLO.get(), 1));
                pOutput.accept(new ItemStack(ModItems.MOREL.get(), 1));
                pOutput.accept(new ItemStack(ModItems.LIONS_MANE.get(), 1));

                // Dried mushrooms
                pOutput.accept(new ItemStack(ModItems.DRIED_BROWN_MUSHROOM.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_RED_MUSHROOM.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_BLUE_ANGEL.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_RUSSULA.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_CHANTERELLE.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_MAGICAL_MUSHROOM.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_FLY_AGARIC.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_DEATH_CAP.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_FREEDOM_CAP.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_OYSTER.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_SHIITAKE.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_KING_TRUMPET.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_PORTOBELLO.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_MOREL.get(), 1));
                pOutput.accept(new ItemStack(ModItems.DRIED_LIONS_MANE.get(), 1));

                // Cultivation materials and stations
                pOutput.accept(new ItemStack(ModItems.DUNG.get(), 1));
                pOutput.accept(new ItemStack(ModItems.TURTLE_DUNG.get(), 1));
                pOutput.accept(new ItemStack(ModBlocks.DUNG_LAYER.get(), 1));
                pOutput.accept(new ItemStack(ModBlocks.TURTLE_DUNG_LAYER.get(), 1));
                pOutput.accept(new ItemStack(ModBlocks.DUNG_BLOCK.get(), 1));
                pOutput.accept(new ItemStack(ModBlocks.TURTLE_DUNG_BLOCK.get(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MULCH_BLOCK.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.TURTLE_MULCH_BLOCK.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_DIRT.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MUSHROOM_DRYING_STATION.get(), 1));

                // Specials
                pOutput.accept(new ItemStack(ModItems.AMALGAMATION.get(), 1));

                // Meals
                pOutput.accept(new ItemStack(ModItems.RAW_BEEF_WELLINGTON.get(), 1));
                pOutput.accept(new ItemStack(ModItems.BEEF_WELLINGTON.get(), 1));
                pOutput.accept(new ItemStack(ModItems.RAW_MUSHROOM_SKEWER.get(), 1));
                pOutput.accept(new ItemStack(ModItems.MUSHROOM_SKEWER.get(), 1));
                pOutput.accept(new ItemStack(ModItems.MUSHROOM_JERKY.get(), 1));
                pOutput.accept(new ItemStack(ModItems.RUST_MUSHROOM_STEW.get(), 1));
                if (ModItems.FARMERS_DELIGHT_LOADED) {
                    pOutput.accept(new ItemStack(ModItems.MUSHROOM_RISOTTO.get(), 1));
                    pOutput.accept(new ItemStack(ModItems.CREAMY_MUSHROOM_PASTA.get(), 1));
                    pOutput.accept(new ItemStack(ModItems.STUFFED_MUSHROOMS.get(), 1));
                }

                // Myceliated wood set
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_SAPLING.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_LOG.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.STRIPPED_MYCELIATED_LOG.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_WOOD.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.STRIPPED_MYCELIATED_WOOD.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_PLANK.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_STAIRS.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_SLAB.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_FENCE.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_FENCE_GATE.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_DOOR.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_TRAPDOOR.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_BUTTON.get().asItem(), 1));
                pOutput.accept(new ItemStack(ModBlocks.MYCELIATED_PRESSURE_PLATE.get().asItem(), 1));
            })
            .build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
