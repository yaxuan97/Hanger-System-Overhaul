package cn.yaxuan97.hangersystemchange.network;

import cn.yaxuan97.hangersystemchange.foodqueue.FoodQueue;
import cn.yaxuan97.hangersystemchange.foodqueue.PlayerFoodQueueProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncFoodQueueToClient {
    private final FoodQueue foodQueue;

    public PacketSyncFoodQueueToClient(FoodQueue foodQueue) {
        this.foodQueue = foodQueue;
    }

    public PacketSyncFoodQueueToClient(FriendlyByteBuf buffer) {
        this.foodQueue = new FoodQueue(buffer);
    }

    public void encode(FriendlyByteBuf buffer) {
        var compoundTag = new CompoundTag();
        foodQueue.saveNBTData(compoundTag);
        buffer.writeNbt(compoundTag);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent(playerFoodQueue -> playerFoodQueue.setFoodQueue(foodQueue));
            }
        }));
    }
}
