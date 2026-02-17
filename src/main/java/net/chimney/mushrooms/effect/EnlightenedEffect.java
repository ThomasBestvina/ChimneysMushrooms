package net.chimney.mushrooms.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EnlightenedEffect extends MobEffect {
    public EnlightenedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
