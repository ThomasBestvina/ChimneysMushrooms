package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DungProjectileSoundHandler {
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof Snowball snowball)) {
            return;
        }

        ItemStack thrownStack = snowball.getItem();
        Item thrownItem = thrownStack.getItem();
        if (thrownItem != ModItems.DUNG.get() && thrownItem != ModItems.TURTLE_DUNG.get()) {
            return;
        }

        Level level = snowball.level();
        if (level.isClientSide) {
            return;
        }

        level.playSound(
                null,
                snowball.getX(),
                snowball.getY(),
                snowball.getZ(),
                SoundEvents.SLIME_SQUISH,
                SoundSource.NEUTRAL,
                0.8F,
                0.9F + level.getRandom().nextFloat() * 0.2F
        );

        if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity() instanceof Sheep sheep) {
                sheep.setColor(DyeColor.BROWN);
            } else if (entityHitResult.getEntity() instanceof Wolf wolf && wolf.isTame()) {
                wolf.setCollarColor(DyeColor.BROWN);
            }
        }
    }
}
