package cn.yaxuan97.hangersystemchange.foodqueue;

import cn.yaxuan97.hangersystemchange.network.PacketSyncFoodQueueToClient;
import cn.yaxuan97.hangersystemchange.setup.Message;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class PlayerFoodQueue {
    public PlayerFoodQueue() {
        foodQueue = new FoodQueue();
    }
    private FoodQueue foodQueue;

    public void addFood(Item foodItem) {
        foodQueue.addFood(foodItem);
    }
    public double getFoodNutrition(Item foodItem) {
        return foodQueue.getFoodNutrition(foodItem);
    }

    public void copyFrom(PlayerFoodQueue playerFoodQueue) {
        this.foodQueue = playerFoodQueue.foodQueue;
    }

    public void saveNBTData(CompoundTag compound) {
        foodQueue.saveNBTData(compound);
    }

    public void loadNBTData(CompoundTag compound) {
        foodQueue.loadNBTData(compound);
    }

    public void setFoodQueue(FoodQueue foodQueue) {
        this.foodQueue = foodQueue;
    }

    public void sync(Player entity) {
        if(entity instanceof ServerPlayer player){
            Message.sendToPlayer(new PacketSyncFoodQueueToClient(foodQueue), player);
        }
    }
}
