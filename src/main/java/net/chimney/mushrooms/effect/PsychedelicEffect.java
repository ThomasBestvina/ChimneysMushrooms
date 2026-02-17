package net.chimney.mushrooms.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public class PsychedelicEffect extends MobEffect {
    public PsychedelicEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF00FF);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Effect is active every tick
    }

    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return true;
            }
        });
    }
}
