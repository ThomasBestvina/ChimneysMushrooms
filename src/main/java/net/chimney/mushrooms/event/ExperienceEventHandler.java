package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.config.ModCommonConfig;
import net.chimney.mushrooms.effect.ModEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExperienceEventHandler {
    @SubscribeEvent
    public static void onPlayerXpChange(PlayerXpEvent.XpChange event)
    {
        Player player = event.getEntity();

        if(player.hasEffect(ModEffects.PSYCHEDELIC_EFFECT.get()))
        {
            int amplifier = player.getEffect(ModEffects.PSYCHEDELIC_EFFECT.get()).getAmplifier()
                    + ModCommonConfig.PSYCHEDELIC_AMPLIFIER_OFFSET.get();
            double multiplier = ModCommonConfig.PSYCHEDELIC_XP_BASE_MULTIPLIER.get()
                    + (ModCommonConfig.PSYCHEDELIC_XP_PER_AMPLIFIER.get() * (amplifier + 1));

            int newAmount = (int)(event.getAmount() * multiplier);
            event.setAmount(newAmount);
        }
    }
}
