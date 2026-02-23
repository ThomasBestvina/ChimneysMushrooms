package net.chimney.mushrooms.worldgen.structure.processor;

import com.mojang.serialization.Codec;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class MycologistHouseProcessor extends StructureProcessor {
    public static final Codec<MycologistHouseProcessor> CODEC = Codec.unit(() -> MycologistHouseProcessor.INSTANCE);
    public static final MycologistHouseProcessor INSTANCE = new MycologistHouseProcessor();

    private static final String MYCOLOGIST_HOUSE_SPAWN_TAG = "chimneysmushrooms.mycologist_house_spawned";
    private static final double SPAWN_DEDUPE_RADIUS = 24.0D;

    private MycologistHouseProcessor() {}

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos pos, BlockPos pivot,
                                                            StructureTemplate.StructureBlockInfo original,
                                                            StructureTemplate.StructureBlockInfo info,
                                                            StructurePlaceSettings settings) {
        if (info.state().is(ModBlocks.MUSHROOM_DRYING_STATION.get()) && level instanceof ServerLevelAccessor serverLevel) {
            // Spawn exactly ONE Mycologist villager per house by deduping within a radius.
            BlockPos spawnPos = info.pos().above();

            AABB checkBox = new AABB(spawnPos).inflate(SPAWN_DEDUPE_RADIUS);
            boolean alreadySpawnedForThisHouse = !serverLevel.getLevel().getEntitiesOfClass(
                    Villager.class,
                    checkBox,
                    v -> v.isAlive() && (v.getTags().contains(MYCOLOGIST_HOUSE_SPAWN_TAG)
                            || v.getVillagerData().getProfession() == ModVillagers.MYCOLOGIST.get())
            ).isEmpty();

            if (!alreadySpawnedForThisHouse) {
                Villager villager = EntityType.VILLAGER.create(serverLevel.getLevel());
                if (villager != null) {
                    villager.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
                    villager.setPersistenceRequired();

                    VillagerType villagerType = VillagerType.byBiome(serverLevel.getBiome(spawnPos));
                    villager.setVillagerData(new VillagerData(villagerType, ModVillagers.MYCOLOGIST.get(), 1));

                    villager.addTag(MYCOLOGIST_HOUSE_SPAWN_TAG);
                    serverLevel.addFreshEntityWithPassengers(villager);
                }
            }
        }
        return info;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessorTypes.MYCOLOGIST_HOUSE_PROCESSOR.get();
    }
}
