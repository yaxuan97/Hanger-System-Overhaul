package cn.yaxuan97.hangersystemchange.mixin;

import cn.yaxuan97.hangersystemchange.foodqueue.PlayerFoodQueueProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraftforge.common.loot.LootModifierManager.LOGGER;

@Mixin(FoodData.class)
public class HangerMixin {
    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true, remap=false)
        public void eat(@NotNull Item foodItem, ItemStack foodItemStack, @org.jetbrains.annotations.Nullable LivingEntity entity, CallbackInfo ci) {
        if (foodItem.isEdible()) {
            if(entity instanceof Player player) {
                var foodproperties = foodItemStack.getFoodProperties(entity);
                player.getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent(playerFoodQueue -> {
                    var foodNutritionChange = playerFoodQueue.getFoodNutrition(foodItem);
                    float foodNutrition;
                    if (foodproperties != null) {
                        foodNutrition = foodproperties.getNutrition() * foodNutritionChange;
                        LOGGER.info("foodNutritionChange: " + foodNutritionChange);
                        player.getFoodData().eat(Math.round(foodNutrition), foodproperties.getSaturationModifier());
                    }
                    playerFoodQueue.addFood(foodItem);
                });
                ci.cancel();
            }
        }
    }
}
