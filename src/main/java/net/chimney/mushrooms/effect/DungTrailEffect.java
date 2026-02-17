package net.chimney.mushrooms.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DungTrailEffect extends MobEffect {
    public DungTrailEffect()
    {
        super(MobEffectCategory.NEUTRAL, 0x7A5901);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return true;
    }
}
