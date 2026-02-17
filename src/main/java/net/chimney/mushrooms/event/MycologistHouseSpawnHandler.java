package net.chimney.mushrooms.event;

import net.chimney.mushrooms.ChimneysMushrooms;
import net.chimney.mushrooms.block.ModBlocks;
import net.chimney.mushrooms.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber(modid = ChimneysMushrooms.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MycologistHouseSpawnHandler {
    private static final ResourceLocation MYCOLOGIST_HOUSE_ID =
            new ResourceLocation(ChimneysMushrooms.MODID, "mycologist_house");
    private static final TagKey<Biome> MYCOLOGIST_HOUSE_BIOMES = TagKey.create(
            Registries.BIOME,
            new ResourceLocation(ChimneysMushrooms.MODID, "has_structure/mycologist_house")
    );
    private static final int MAX_RETRIES = 200;
    private static final int LAND_SEARCH_RADIUS = 24;
    private static final int CENTER_SEARCH_RADIUS = 10;
    private static final Queue<PendingSpawn> PENDING_SPAWNS = new ConcurrentLinkedQueue<>();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!event.isNewChunk()) {
            return;
        }

        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getChunk() instanceof LevelChunk)) {
            return;
        }

        ChunkAccess chunk = event.getChunk();
        var structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        for (Map.Entry<Structure, StructureStart> entry : chunk.getAllStarts().entrySet()) {
            StructureStart start = entry.getValue();
            if (start == null || !start.isValid()) {
                continue;
            }

            ResourceLocation structureId = structureRegistry.getKey(entry.getKey());
            if (!MYCOLOGIST_HOUSE_ID.equals(structureId)) {
                continue;
            }

            BlockPos center = start.getBoundingBox().getCenter();
            PENDING_SPAWNS.add(new PendingSpawn(
                    level.dimension(),
                    center.getX(),
                    center.getZ(),
                    start.getBoundingBox().minX(),
                    start.getBoundingBox().maxX(),
                    start.getBoundingBox().minY(),
                    start.getBoundingBox().maxY(),
                    start.getBoundingBox().minZ(),
                    start.getBoundingBox().maxZ(),
                    0
            ));
            return;
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING_SPAWNS.isEmpty()) {
            return;
        }

        MinecraftServer server = event.getServer();
        int toProcess = PENDING_SPAWNS.size();

        for (int i = 0; i < toProcess; i++) {
            PendingSpawn pending = PENDING_SPAWNS.poll();
            if (pending == null) {
                return;
            }

            ServerLevel level = server.getLevel(pending.dimension());
            if (level == null) {
                continue;
            }

            BlockPos probePos = new BlockPos(pending.x(), 0, pending.z());
            if (!level.hasChunkAt(probePos)) {
                if (pending.retries() < MAX_RETRIES) {
                    PENDING_SPAWNS.add(pending.nextRetry());
                }
                continue;
            }

            BlockPos spawnPos = findLandSpawnPos(level, pending);
            if (spawnPos == null) {
                if (pending.retries() < MAX_RETRIES) {
                    PENDING_SPAWNS.add(pending.nextRetry());
                }
                continue;
            }

            spawnOrConvertMycologist(level, pending, spawnPos);
        }
    }

    private static void spawnOrConvertMycologist(ServerLevel level, PendingSpawn pending, BlockPos spawnPos) {
        AABB structureBounds = new AABB(
                pending.minX(),
                level.getMinBuildHeight(),
                pending.minZ(),
                pending.maxX() + 1,
                level.getMaxBuildHeight(),
                pending.maxZ() + 1
        );

        Villager villager = level.getEntitiesOfClass(Villager.class, structureBounds).stream()
                .filter(existing -> !existing.isBaby())
                .min(Comparator.comparingDouble(existing -> existing.distanceToSqr(
                        spawnPos.getX() + 0.5D,
                        spawnPos.getY(),
                        spawnPos.getZ() + 0.5D
                )))
                .orElse(null);

        if (villager == null) {
            villager = EntityType.VILLAGER.create(level);
            if (villager == null) {
                return;
            }

            villager.moveTo(
                    spawnPos.getX() + 0.5D,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5D,
                    level.random.nextFloat() * 360.0F,
                    0.0F
            );
            level.addFreshEntity(villager);
        }

        VillagerType villagerType = VillagerType.byBiome(level.getBiome(spawnPos));
        VillagerData mycologistData = new VillagerData(villagerType, ModVillagers.MYCOLOGIST.get(), 1);
        villager.setVillagerData(mycologistData);
        villager.setVillagerXp(0);
        villager.setPersistenceRequired();
    }

    private static BlockPos findLandSpawnPos(ServerLevel level, PendingSpawn pending) {
        BlockPos nearWorkstation = findSpawnNearWorkstation(level, pending);
        if (nearWorkstation != null) {
            return nearWorkstation;
        }
        BlockPos fallback = findFallbackLandSpawn(level, pending);
        if (fallback != null) {
            return fallback;
        }
        return findGuaranteedSpawnInBounds(level, pending);
    }

    private static BlockPos findSpawnNearWorkstation(ServerLevel level, PendingSpawn pending) {
        for (int y = pending.minY(); y <= pending.maxY(); y++) {
            for (int x = pending.minX(); x <= pending.maxX(); x++) {
                for (int z = pending.minZ(); z <= pending.maxZ(); z++) {
                    BlockPos workstationPos = new BlockPos(x, y, z);
                    if (!level.hasChunkAt(workstationPos) || !level.getBlockState(workstationPos).is(ModBlocks.MUSHROOM_DRYING_STATION.get())) {
                        continue;
                    }

                    int preferredFeetY = workstationPos.getY() + 1;
                    BlockPos centeredSameY = findCenteredSpawnNearY(level, pending, preferredFeetY);
                    if (centeredSameY != null) {
                        return centeredSameY;
                    }

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        BlockPos candidate = workstationPos.relative(direction).above();
                        if (candidate.getX() < pending.minX() || candidate.getX() > pending.maxX()
                                || candidate.getZ() < pending.minZ() || candidate.getZ() > pending.maxZ()) {
                            continue;
                        }
                        if (isValidLandSpawn(level, candidate)) {
                            return candidate;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static BlockPos findCenteredSpawnNearY(ServerLevel level, PendingSpawn pending, int preferredY) {
        int centerX = pending.x();
        int centerZ = pending.z();
        int minY = Math.max(pending.minY(), preferredY - 1);
        int maxY = Math.min(pending.maxY() + 1, preferredY + 1);

        for (int radius = 0; radius <= CENTER_SEARCH_RADIUS; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (radius > 0 && Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue;
                    }

                    int x = centerX + dx;
                    int z = centerZ + dz;
                    if (x < pending.minX() || x > pending.maxX() || z < pending.minZ() || z > pending.maxZ()) {
                        continue;
                    }

                    for (int y = minY; y <= maxY; y++) {
                        BlockPos candidate = new BlockPos(x, y, z);
                        if (!level.hasChunkAt(candidate)) {
                            continue;
                        }
                        if (isValidLandSpawn(level, candidate)) {
                            return candidate;
                        }
                    }
                }
            }
        }

        return null;
    }

    private static BlockPos findFallbackLandSpawn(ServerLevel level, PendingSpawn pending) {
        int centerX = pending.x();
        int centerZ = pending.z();

        for (int radius = 0; radius <= LAND_SEARCH_RADIUS; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (radius > 0 && Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue;
                    }

                    int x = centerX + dx;
                    int z = centerZ + dz;
                    if (x < pending.minX() || x > pending.maxX() || z < pending.minZ() || z > pending.maxZ()) {
                        continue;
                    }

                    BlockPos columnPos = new BlockPos(x, 0, z);
                    if (!level.hasChunkAt(columnPos)) {
                        continue;
                    }

                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    BlockPos feetPos = new BlockPos(x, y, z);
                    if (isValidLandSpawn(level, feetPos)) {
                        return feetPos;
                    }
                }
            }
        }
        return null;
    }

    private static BlockPos findGuaranteedSpawnInBounds(ServerLevel level, PendingSpawn pending) {
        int centerX = pending.x();
        int centerZ = pending.z();

        for (int radius = 0; radius <= CENTER_SEARCH_RADIUS; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (radius > 0 && Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue;
                    }

                    int x = centerX + dx;
                    int z = centerZ + dz;
                    if (x < pending.minX() || x > pending.maxX() || z < pending.minZ() || z > pending.maxZ()) {
                        continue;
                    }

                    for (int y = pending.minY(); y <= pending.maxY(); y++) {
                        BlockPos candidate = new BlockPos(x, y, z);
                        if (!level.hasChunkAt(candidate)) {
                            continue;
                        }
                        if (isValidLandSpawn(level, candidate)) {
                            return candidate;
                        }
                    }
                }
            }
        }

        return null;
    }

    private static boolean isAllowedHouseBiome(ServerLevel level, BlockPos pos) {
        return level.getBiome(pos).is(MYCOLOGIST_HOUSE_BIOMES);
    }

    private static boolean isValidLandSpawn(ServerLevel level, BlockPos feetPos) {
        BlockPos belowPos = feetPos.below();
        BlockPos headPos = feetPos.above();

        if (!level.getFluidState(belowPos).isEmpty()) {
            return false;
        }
        if (!level.getFluidState(feetPos).isEmpty() || !level.getFluidState(headPos).isEmpty()) {
            return false;
        }

        if (!level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
            return false;
        }

        if (!level.getBlockState(feetPos).getCollisionShape(level, feetPos).isEmpty()) {
            return false;
        }
        return level.getBlockState(headPos).getCollisionShape(level, headPos).isEmpty();
    }

    private record PendingSpawn(
            ResourceKey<Level> dimension,
            int x,
            int z,
            int minX,
            int maxX,
            int minY,
            int maxY,
            int minZ,
            int maxZ,
            int retries
    ) {
        private PendingSpawn nextRetry() {
            return new PendingSpawn(dimension, x, z, minX, maxX, minY, maxY, minZ, maxZ, retries + 1);
        }
    }
}
