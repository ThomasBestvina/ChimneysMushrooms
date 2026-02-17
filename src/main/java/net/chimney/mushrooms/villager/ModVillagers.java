package net.chimney.mushrooms.villager;

import com.google.common.collect.ImmutableSet;
import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ChimneysMushrooms.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ChimneysMushrooms.MODID);

    public static final RegistryObject<PoiType> MYCOLOGOIST_POI = POI_TYPES.register("mycologist_poi", () -> new PoiType(ImmutableSet.copyOf(ModBlocks.MUSHROOM_DRYING_STATION.get().getStateDefinition().getPossibleStates()), 1,1));

    public static final RegistryObject<VillagerProfession> MYCOLOGIST = VILLAGER_PROFESSION.register("mycologist", () -> new VillagerProfession("mycologist",
            holder -> holder.get() == MYCOLOGOIST_POI.get(),
            holder -> holder.get() == MYCOLOGOIST_POI.get(),
            ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FARMER));

    public static void register(IEventBus eventBus){
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSION.register(eventBus);
    }

}
