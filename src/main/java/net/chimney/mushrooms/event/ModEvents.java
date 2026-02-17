package net.chimney.mushrooms.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.item.ModItems;
import net.chimney.mushrooms.villager.ModVillagers;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID)
public class ModEvents {
    private static VillagerTrades.ItemListing sell(ItemStack cost, ItemStack result, int maxUses, int villagerXp, float priceMultiplier) {
        return (pTrader, pRandom) -> new MerchantOffer(cost, result, maxUses, villagerXp, priceMultiplier);
    }

    private static VillagerTrades.ItemListing buy(ItemStack input, ItemStack emeralds, int maxUses, int villagerXp, float priceMultiplier) {
        return (pTrader, pRandom) -> new MerchantOffer(input, emeralds, maxUses, villagerXp, priceMultiplier);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.MYCOLOGIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // level 1
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.BROWN_MUSHROOM, 5), 16, 2, 0.02f));
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.RED_MUSHROOM, 5), 16, 2, 0.02f));
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.DUNG.get(), 12), 16, 2, 0.02f));
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModBlocks.MULCH_BLOCK.get(), 6), 12, 2, 0.02f));
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.GRAIN_BAG.get(), 1), 12, 2, 0.02f));
            trades.get(1).add(sell(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.BONE_MEAL, 6), 16, 2, 0.02f));
            trades.get(1).add(buy(new ItemStack(ModItems.DUNG.get(), 24), new ItemStack(Items.EMERALD, 1), 16, 2, 0.02f));

            // level 2
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.CHANTERELLE.get(), 3), 12, 5, 0.02f));
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.OYSTER.get(), 3), 12, 5, 0.02f));
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.SHIITAKE.get(), 3), 12, 5, 0.02f));
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.PORTOBELLO.get(), 3), 12, 5, 0.02f));
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.KING_TRUMPET.get(), 3), 12, 5, 0.02f));
            trades.get(2).add(sell(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.MYCELIUM, 2), 12, 5, 0.02f));
            trades.get(2).add(buy(new ItemStack(ModItems.CHANTERELLE.get(), 12), new ItemStack(Items.EMERALD, 1), 12, 5, 0.02f));
            trades.get(2).add(buy(new ItemStack(ModItems.OYSTER.get(), 12), new ItemStack(Items.EMERALD, 1), 12, 5, 0.02f));
            trades.get(2).add(buy(new ItemStack(ModItems.SHIITAKE.get(), 12), new ItemStack(Items.EMERALD, 1), 12, 5, 0.02f));

            // level 3
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.MOREL.get(), 2), 10, 10, 0.02f));
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.LIONS_MANE.get(), 2), 10, 10, 0.02f));
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 6), new ItemStack(ModItems.FLY_AGARIC.get(), 2), 10, 10, 0.02f));
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.DRIED_OYSTER.get(), 3), 10, 10, 0.02f));
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.DRIED_SHIITAKE.get(), 3), 10, 10, 0.02f));
            trades.get(3).add(sell(new ItemStack(Items.EMERALD, 4), new ItemStack(Items.PODZOL, 2), 10, 10, 0.02f));
            trades.get(3).add(buy(new ItemStack(ModItems.MOREL.get(), 8), new ItemStack(Items.EMERALD, 1), 10, 10, 0.02f));
            trades.get(3).add(buy(new ItemStack(ModItems.LIONS_MANE.get(), 8), new ItemStack(Items.EMERALD, 1), 10, 10, 0.02f));

            // level 4
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.RUSSULA.get(), 2), 8, 15, 0.02f));
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.MAGICAL_MUSHROOM.get(), 1), 6, 15, 0.02f));
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.DEATH_CAP.get(), 1), 6, 15, 0.02f));
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.FREEDOM_CAP.get(), 1), 6, 15, 0.02f));
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 6), new ItemStack(ModItems.DRIED_RUSSULA.get(), 2), 8, 15, 0.02f));
            trades.get(4).add(sell(new ItemStack(Items.EMERALD, 6), new ItemStack(Items.SUSPICIOUS_STEW, 1), 8, 15, 0.02f));
            trades.get(4).add(buy(new ItemStack(ModItems.RUSSULA.get(), 4), new ItemStack(Items.EMERALD, 1), 8, 15, 0.02f));
            trades.get(4).add(buy(new ItemStack(ModItems.FLY_AGARIC.get(), 4), new ItemStack(Items.EMERALD, 1), 8, 15, 0.02f));

            // level 5
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 6), new ItemStack(ModItems.MUSHROOM_JERKY.get(), 4), 8, 30, 0.02f));
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 7), new ItemStack(ModItems.RUST_MUSHROOM_STEW.get(), 1), 8, 30, 0.02f));
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.MUSHROOM_SKEWER.get(), 2), 8, 30, 0.02f));
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 9), new ItemStack(ModItems.BEEF_WELLINGTON.get(), 1), 6, 30, 0.02f));
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.DRIED_LIONS_MANE.get(), 3), 8, 30, 0.02f));
            trades.get(5).add(sell(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.DRIED_MOREL.get(), 3), 8, 30, 0.02f));
            trades.get(5).add(buy(new ItemStack(ModItems.DRIED_RUSSULA.get(), 6), new ItemStack(Items.EMERALD, 2), 6, 30, 0.02f));
            trades.get(5).add(buy(new ItemStack(ModItems.DRIED_MOREL.get(), 6), new ItemStack(Items.EMERALD, 2), 6, 30, 0.02f));
            trades.get(5).add(buy(new ItemStack(ModItems.MUSHROOM_JERKY.get(), 8), new ItemStack(Items.EMERALD, 1), 8, 30, 0.02f));
        }
    }

    @SubscribeEvent
    public static void addCustomWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(ModItems.MAGICAL_MUSHROOM.get(), 1),
                2, 12, 0.15f));
        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 16),
                new ItemStack(ModItems.FREEDOM_CAP.get(), 1),
                2, 12, 0.15f));
    }
}
