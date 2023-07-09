package cn.yaxuan97.hangersystemchange.eventhandler;

import cn.yaxuan97.hangersystemchange.foodqueue.PlayerFoodQueueProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;


public class AppleSkinEventHandler {
    @SubscribeEvent
    public void onFoodValuesEvent(FoodValuesEvent event) {
        event.player.getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY)
                .ifPresent(playerFoodQueue -> {
                    var foodItem = event.itemStack.getItem();
                    var foodNutritionChange = playerFoodQueue.getFoodNutrition(foodItem);
                    event.modifiedFoodValues = new FoodValues(
                            (int) Math.floor(event.modifiedFoodValues.hunger * foodNutritionChange),
                            event.modifiedFoodValues.saturationModifier
                    );
                });
    }
}