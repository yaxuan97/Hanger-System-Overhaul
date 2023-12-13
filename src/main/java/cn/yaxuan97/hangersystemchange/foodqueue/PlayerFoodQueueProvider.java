package cn.yaxuan97.hangersystemchange.foodqueue;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerFoodQueueProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerFoodQueue> PLAYER_FOOD_QUEUE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private PlayerFoodQueue playerFoodQueue = null;
    private final LazyOptional<PlayerFoodQueue> lazyOptional = LazyOptional.of(this::createPlayerFoodQueue);
    @Nonnull
    private PlayerFoodQueue createPlayerFoodQueue() {
        if (playerFoodQueue == null) {
            playerFoodQueue = new PlayerFoodQueue();
        }
        return playerFoodQueue;
    }
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == PLAYER_FOOD_QUEUE_CAPABILITY ? lazyOptional.cast() : LazyOptional.empty();
    }
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }
    @Override
    public CompoundTag serializeNBT() {
        var nbt = new CompoundTag();
        createPlayerFoodQueue().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerFoodQueue().loadNBTData(nbt);
    }
}
