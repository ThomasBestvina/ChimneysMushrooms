package net.chimney.mushrooms.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BlueAngelEffect extends MobEffect {
    public BlueAngelEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1E90FF);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
                "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC",
                1.0D,
                AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
