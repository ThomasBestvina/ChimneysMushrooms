package net.chimney.mushrooms.block.entity;

import net.chimney.mushrooms.block.custom.InoculatedLogBlock;
import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.Connection;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InoculatedLogBlockEntity extends BlockEntity {
    private static final String MUSHROOM_TYPE_KEY = "MushroomType";
    private static final String ORIGINAL_LOG_BLOCK_KEY = "OriginalLogBlock";
    private static final String ORIGINAL_LOG_PROPERTIES_KEY = "OriginalLogProperties";
    private MushroomType mushroomType = MushroomType.LIONS_MANE; // Default to Lion's Mane
    @Nullable
    private BlockState originalLogState = null;
    public static final ModelProperty<BlockState> ORIGINAL_LOG_STATE = new ModelProperty<>();

    private static BlockEntityType<? extends InoculatedLogBlockEntity> TYPE;

    public InoculatedLogBlockEntity(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    public static void setType(BlockEntityType<? extends InoculatedLogBlockEntity> type) {
        TYPE = type;
    }

    public static BlockEntityType<? extends InoculatedLogBlockEntity> getStaticType() {
        if (TYPE == null) {
            throw new IllegalStateException("BlockEntityType not initialized!");
        }
        return TYPE;
    }

    @Override
    public BlockEntityType<?> getType() {
        return TYPE;
    }

    public MushroomType getMushroomType() {
        return mushroomType;
    }

    public @Nullable BlockState getOriginalLogState() {
        return originalLogState;
    }

    public void setOriginalLogState(BlockState state) {
        this.originalLogState = state;
        setChanged();
        if (level != null) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void setMushroomType(MushroomType type) {
        this.mushroomType = type;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString(MUSHROOM_TYPE_KEY, mushroomType.name());
        if (originalLogState != null) {
            tag.putString(ORIGINAL_LOG_BLOCK_KEY, BuiltInRegistries.BLOCK.getKey(originalLogState.getBlock()).toString());

            CompoundTag propertiesTag = new CompoundTag();
            for (Property<?> property : originalLogState.getProperties()) {
                propertiesTag.putString(property.getName(), getPropertyValueName(originalLogState, property));
            }
            tag.put(ORIGINAL_LOG_PROPERTIES_KEY, propertiesTag);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(MUSHROOM_TYPE_KEY)) {
            try {
                mushroomType = MushroomType.valueOf(tag.getString(MUSHROOM_TYPE_KEY));
            } catch (IllegalArgumentException e) {
                mushroomType = MushroomType.LIONS_MANE;
            }
        }

        if (tag.contains(ORIGINAL_LOG_BLOCK_KEY)) {
            BlockState loadedState = null;
            ResourceLocation blockId = new ResourceLocation(tag.getString(ORIGINAL_LOG_BLOCK_KEY));
            if (BuiltInRegistries.BLOCK.containsKey(blockId)) {
                loadedState = BuiltInRegistries.BLOCK.get(blockId).defaultBlockState();
            }

            if (loadedState != null && tag.contains(ORIGINAL_LOG_PROPERTIES_KEY)) {
                CompoundTag propertiesTag = tag.getCompound(ORIGINAL_LOG_PROPERTIES_KEY);
                loadedState = applyStoredProperties(loadedState, propertiesTag);
            }
            originalLogState = loadedState;
        } else {
            originalLogState = null;
        }
    }

    @Override
    public @NotNull ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        if (originalLogState != null) {
            builder.with(ORIGINAL_LOG_STATE, originalLogState);
        }
        return builder.build();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            load(pkt.getTag());
            requestModelDataUpdate();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    private static <T extends Comparable<T>> String getPropertyValueName(BlockState state, Property<T> property) {
        return property.getName(state.getValue(property));
    }

    private static BlockState applyStoredProperties(BlockState state, CompoundTag propertiesTag) {
        BlockState current = state;
        for (String key : propertiesTag.getAllKeys()) {
            Property<?> property = current.getBlock().getStateDefinition().getProperty(key);
            if (property != null) {
                current = applyPropertyValue(current, property, propertiesTag.getString(key));
            }
        }
        return current;
    }

    private static <T extends Comparable<T>> BlockState applyPropertyValue(BlockState state, Property<T> property, String valueName) {
        return property.getValue(valueName).map(value -> state.setValue(property, value)).orElse(state);
    }
}
