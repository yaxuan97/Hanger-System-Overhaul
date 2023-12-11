package cn.yaxuan97.hangersystemchange.foodqueue;

import cn.yaxuan97.hangersystemchange.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.yaxuan97.hangersystemchange.HangerSystemChangeMod.LOGGER;

public class FoodQueue {
    public FoodQueue() {
        foodQueueQuantity = new LinkedList<>();
        foodQueueCategory = new LinkedList<>();
        foodQueueQuantityWithBlacklist = new LinkedList<>();
        foodQueueCategoryWithBlacklist = new LinkedList<>();
        foodNutritionDecreaseQuantityMap = new HashMap<>();
        foodNutritionDecreaseCategoryMap = new HashMap<>();
    }

    public FoodQueue(FriendlyByteBuf buffer) {
        foodQueueQuantity = new LinkedList<>();
        foodQueueCategory = new LinkedList<>();
        foodQueueQuantityWithBlacklist = new LinkedList<>();
        foodQueueCategoryWithBlacklist = new LinkedList<>();
        foodNutritionDecreaseQuantityMap = new HashMap<>();
        foodNutritionDecreaseCategoryMap = new HashMap<>();
        var compoundTag = buffer.readAnySizeNbt();
        if (compoundTag != null) {
            this.loadNBTData(compoundTag);
        }
    }

    private final Queue<ResourceLocation> foodQueueQuantity;
    private final Queue<ResourceLocation> foodQueueCategory;
    private final Queue<ResourceLocation> foodQueueQuantityWithBlacklist;
    private final Queue<ResourceLocation> foodQueueCategoryWithBlacklist;

    private final Map<ResourceLocation, Integer> foodNutritionDecreaseQuantityMap;
    private final Map<ResourceLocation, Integer> foodNutritionDecreaseCategoryMap;

    private void foodNutritionQuantityDecrease(ResourceLocation foodItemKey) {
        int n;
        if (foodNutritionDecreaseQuantityMap.containsKey(foodItemKey)) {
            n = foodNutritionDecreaseQuantityMap.get(foodItemKey) - 1;
        } else {
            n = 1;
        }
        foodNutritionDecreaseQuantityMap.put(foodItemKey, n);
        LOGGER.debug("foodNutritionDecrease: " + foodItemKey + " " + n + " -> " + foodNutritionDecreaseQuantityMap.get(foodItemKey));
    }

    private void foodNutritionCategoryDecrease(ResourceLocation foodItemKey) {
        int n;
        if (foodNutritionDecreaseCategoryMap.containsKey(foodItemKey)) {
            n = foodNutritionDecreaseCategoryMap.get(foodItemKey) - 1;
        } else {
            n = 1;
        }
        foodNutritionDecreaseCategoryMap.put(foodItemKey, n);
        LOGGER.debug("foodNutritionDecrease: " + foodItemKey + " " + n + " -> " + foodNutritionDecreaseCategoryMap.get(foodItemKey));
    }

    private void foodNutritionCategoryIncrease(ResourceLocation foodItemKey) {
        int n;
        if (foodNutritionDecreaseCategoryMap.containsKey(foodItemKey)) {
            n = foodNutritionDecreaseCategoryMap.get(foodItemKey) + 1;
        } else {
            n = 1;
        }
        foodNutritionDecreaseCategoryMap.put(foodItemKey, n);
        LOGGER.debug("foodNutritionIncrease: " + foodItemKey + " " + n + " -> " + foodNutritionDecreaseCategoryMap.get(foodItemKey));
    }

    private void foodNutritionQuantityIncrease(ResourceLocation foodItemKey) {
        int n;
        if (foodNutritionDecreaseQuantityMap.containsKey(foodItemKey)) {
            n = foodNutritionDecreaseQuantityMap.get(foodItemKey) + 1;
        } else {
            n = 1;
        }
        foodNutritionDecreaseQuantityMap.put(foodItemKey, n);
        LOGGER.debug("foodNutritionIncrease: " + foodItemKey + " " + n + " -> " + foodNutritionDecreaseQuantityMap.get(foodItemKey));
    }

    public float getFoodNutrition(ResourceLocation foodItemKey) {
        if (Config.ignoredFoods.contains(foodItemKey)) {
            return 1.0f;
        }
        if (Config.whitelistCanRefresh.contains(foodItemKey)) {
            return 1.0f;
        }
        return (float) (1.0f *
                        Math.pow(Config.foodNutritionDecreaseByQuantity, foodNutritionDecreaseQuantityMap.getOrDefault(foodItemKey, 1)) *
                        Math.pow(Config.foodNutritionDecreaseByCategory, foodNutritionDecreaseCategoryMap.getOrDefault(foodItemKey, 1)));
    }

    public float getFoodNutrition(Item foodItem) {
        return getFoodNutrition(ForgeRegistries.ITEMS.getKey(foodItem));
    }

    public boolean clearEnough() {
        boolean isEnough = false;
        if (Config.foodQueueQuantityLength < foodQueueQuantityWithBlacklist.size()) {
            var top = foodQueueQuantityWithBlacklist.poll();
            if (Config.blacklist.contains(top)) {
                foodNutritionQuantityDecrease(top);
                isEnough = true;
            }
        }
        if (Config.foodQueueCategoryLength < foodQueueCategoryWithBlacklist.size()) {
            var top = foodQueueCategoryWithBlacklist.poll();
            if (Config.blacklist.contains(top)) {
                foodNutritionCategoryDecrease(top);
                isEnough = true;
            }
        }
        if (Config.foodQueueQuantityLength < foodQueueQuantity.size()) {
            foodNutritionQuantityDecrease(foodQueueQuantity.poll());
            isEnough = true;
        }
        if (Config.foodQueueCategoryLength < foodQueueCategory.size()) {
            foodNutritionCategoryDecrease(foodQueueCategory.poll());
            isEnough = true;
        }
        return isEnough;
    }

    public void addFood(ResourceLocation foodItemKey) {
        if (Config.ignoredFoods.contains(foodItemKey)) {
            return;
        }
        foodQueueQuantityWithBlacklist.offer(foodItemKey);
        foodNutritionQuantityIncrease(foodItemKey);
        if (!foodQueueCategoryWithBlacklist.contains(foodItemKey)) {
            foodQueueCategoryWithBlacklist.offer(foodItemKey);
            foodNutritionCategoryIncrease(foodItemKey);
        }
        if (!Config.blacklist.contains(foodItemKey)) {
            foodQueueQuantity.offer(foodItemKey);
            if (!foodQueueCategory.contains(foodItemKey)) {
                foodQueueCategory.offer(foodItemKey);
            }
        }
        clearEnough();
    }

    public void addFood(Item foodItem) {
        var foodItemKey = ForgeRegistries.ITEMS.getKey(foodItem);
        if (foodItemKey != null) {
            addFood(foodItemKey);
        }
    }

    public void saveNBTData(CompoundTag compound) {
        var foodQueueQuantityCompound = new CompoundTag();
        var index = new AtomicInteger();
        foodQueueQuantity.forEach(foodItemKey -> foodQueueQuantityCompound.putString("queue" + index.getAndIncrement(), foodItemKey.toString()));
        compound.put("foodQueueQuantity", foodQueueQuantityCompound);
        var foodQueueCategoryCompound = new CompoundTag();
        index.set(0);
        foodQueueCategory.forEach(foodItemKey -> foodQueueCategoryCompound.putString("queue" + index.getAndIncrement(), foodItemKey.toString()));
        compound.put("foodQueueCategory", foodQueueCategoryCompound);
        var foodQueueQuantityWithBlacklistCompound = new CompoundTag();
        index.set(0);
        foodQueueQuantityWithBlacklist.forEach(foodItemKey -> foodQueueQuantityWithBlacklistCompound.putString("queue" + index.getAndIncrement(), foodItemKey.toString()));
        compound.put("foodQueueQuantityWithBlacklist", foodQueueQuantityWithBlacklistCompound);
        var foodQueueCategoryWithBlacklistCompound = new CompoundTag();
        index.set(0);
        foodQueueCategoryWithBlacklist.forEach(foodItemKey -> foodQueueCategoryWithBlacklistCompound.putString("queue" + index.getAndIncrement(), foodItemKey.toString()));
        compound.put("foodQueueCategoryWithBlacklist", foodQueueCategoryWithBlacklistCompound);
        var foodNutritionQuantityDecreaseMapCompound = new CompoundTag();
        foodNutritionDecreaseQuantityMap.forEach((foodItemKey, nutrition) -> foodNutritionQuantityDecreaseMapCompound.putInt(foodItemKey.toString(), nutrition));
        var foodNutritionCategoryDecreaseMapCompound = new CompoundTag();
        foodNutritionDecreaseCategoryMap.forEach((foodItemKey, nutrition) -> foodNutritionCategoryDecreaseMapCompound.putInt(foodItemKey.toString(), nutrition));
        compound.put("foodNutritionDecreaseMap", foodNutritionQuantityDecreaseMapCompound);
        LOGGER.debug("saveNBTData:");
        LOGGER.debug("foodNutritionDecreaseQuantityMap: " + foodNutritionQuantityDecreaseMapCompound);
        LOGGER.debug("foodNutritionDecreaseCategoryMap: " + foodNutritionCategoryDecreaseMapCompound);
        LOGGER.debug("foodQueueQuantity: " + foodQueueQuantityCompound);
        LOGGER.debug("foodQueueCategory: " + foodQueueCategoryCompound);
        LOGGER.debug("foodQueueQuantityWithBlacklist: " + foodQueueQuantityWithBlacklistCompound);
        LOGGER.debug("foodQueueCategoryWithBlacklist: " + foodQueueCategoryWithBlacklistCompound);
    }

    public void loadNBTData(CompoundTag compound) {
        var foodQueueQuantityCompound = compound.getCompound("foodQueueQuantity");
        for (int i = 0; foodQueueQuantityCompound.contains("queue" + i); i++) {
            foodQueueQuantity.add(new ResourceLocation(foodQueueQuantityCompound.getString("queue" + i)));
        }
        var foodQueueCategoryCompound = compound.getCompound("foodQueueCategory");
        for (int i = 0; foodQueueCategoryCompound.contains("queue" + i); i++) {
            foodQueueCategory.add(new ResourceLocation(foodQueueCategoryCompound.getString("queue" + i)));
        }
        var foodQueueQuantityWithBlacklistCompound = compound.getCompound("foodQueueQuantityWithBlacklist");
        for (int i = 0; foodQueueQuantityWithBlacklistCompound.contains("queue" + i); i++) {
            foodQueueQuantityWithBlacklist.add(new ResourceLocation(foodQueueQuantityWithBlacklistCompound.getString("queue" + i)));
        }
        var foodQueueCategoryWithBlacklistCompound = compound.getCompound("foodQueueCategoryWithBlacklist");
        for (int i = 0; foodQueueCategoryWithBlacklistCompound.contains("queue" + i); i++) {
            foodQueueCategoryWithBlacklist.add(new ResourceLocation(foodQueueCategoryWithBlacklistCompound.getString("queue" + i)));
        }
        var foodNutritionDecreaseMapQuantityCompound = compound.getCompound("foodNutritionDecreaseMap");
        foodNutritionDecreaseMapQuantityCompound.getAllKeys().forEach(foodItemKey -> foodNutritionDecreaseQuantityMap.put(new ResourceLocation(foodItemKey), foodNutritionDecreaseMapQuantityCompound.getInt(foodItemKey)));
        var foodNutritionDecreaseMapCategoryCompound = compound.getCompound("foodNutritionDecreaseMap");
        foodNutritionDecreaseMapCategoryCompound.getAllKeys().forEach(foodItemKey -> foodNutritionDecreaseCategoryMap.put(new ResourceLocation(foodItemKey), foodNutritionDecreaseMapCategoryCompound.getInt(foodItemKey)));

        while (clearEnough()) ;
        LOGGER.debug("loadNBTData");
        LOGGER.debug("foodNutritionDecreaseQuantityMap: " + foodNutritionDecreaseMapQuantityCompound);
        LOGGER.debug("foodNutritionDecreaseCategoryMap: " + foodNutritionDecreaseMapCategoryCompound);
        LOGGER.debug("foodQueueQuantity: " + foodQueueQuantityCompound);
        LOGGER.debug("foodQueueCategory: " + foodQueueCategoryCompound);
        LOGGER.debug("foodQueueQuantityWithBlacklist: " + foodQueueQuantityWithBlacklistCompound);
        LOGGER.debug("foodQueueCategoryWithBlacklist: " + foodQueueCategoryWithBlacklistCompound);
    }
}
