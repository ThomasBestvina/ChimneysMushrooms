package net.chimney.mushrooms.block.entity;

import net.chimney.mushrooms.mushroom.MushroomType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InoculatedSubstrateBlockEntity extends BlockEntity {
    private static final String MUSHROOM_TYPE_KEY = "MushroomType";
    private MushroomType mushroomType = MushroomType.BROWN;

    private static BlockEntityType<? extends InoculatedSubstrateBlockEntity> TYPE;

    public InoculatedSubstrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    public static void setType(BlockEntityType<? extends InoculatedSubstrateBlockEntity> type) {
        TYPE = type;
    }

    public static BlockEntityType<? extends InoculatedSubstrateBlockEntity> getStaticType() {
        if (TYPE == null) {
            throw new IllegalStateException("BlockEntityType not initialized yet! Make sure ModBlockEntities is registered properly.");
        }
        return TYPE;
    }

    @Override
    public BlockEntityType<?> getType() {
        return TYPE; // Return the static TYPE
    }

    public MushroomType getMushroomType() {
        return mushroomType;
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
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(MUSHROOM_TYPE_KEY)) {
            try {
                mushroomType = MushroomType.valueOf(tag.getString(MUSHROOM_TYPE_KEY));
            } catch (IllegalArgumentException e) {
                mushroomType = MushroomType.BROWN;
            }
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putString(MUSHROOM_TYPE_KEY, mushroomType.name());
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
