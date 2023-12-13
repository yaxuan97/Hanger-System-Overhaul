package cn.yaxuan97.hangersystemchange;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = HangerSystemChangeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue FOOD_QUEUE_QUANTITY_LENGTH = BUILDER
            .translation("config.hangerSystemChange.foodQueueQuantityLength")
            .defineInRange("quantityLength", 20, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue FOOD_QUEUE_CATEGORY_LENGTH = BUILDER
            .translation("config.hangerSystemChange.foodQueueCategoryLength")
            .defineInRange("categoryLength", 10, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue FOOD_NUTRITION_DECREASE_BY_QUANTITY = BUILDER
            .translation("config.hangerSystemChange.foodNutritionDecreaseByQuantity")
            .defineInRange("decreaseByQuantity", 0.75, 0, 1);
    public static final ForgeConfigSpec.DoubleValue FOOD_NUTRITION_DECREASE_BY_CATEGORY = BUILDER
            .translation("config.hangerSystemChange.foodNutritionDecreaseByCategory")
            .defineInRange("decreaseByCategory", 0.8, 0, 1);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITELIST_CAN_REFRESH = BUILDER
            .translation("config.hangerSystemChange.whitelistCanRefresh")
            .defineListAllowEmpty(Collections.singletonList("whitelist"), List::of, Config::validateItemName);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST = BUILDER
            .translation("config.hangerSystemChange.blacklist")
            .defineListAllowEmpty(Collections.singletonList("blacklist"), () -> List.of(
                    "minecraft:cookie",
                    "minecraft:glow_berries",
                    "minecraft:honey_bottle",
                    "minecraft:pufferfish",
                    "minecraft:salmon",
                    "minecraft:rotten_flesh",
                    "minecraft:spider_eye",
                    "minecraft:sweet_berries",
                    "minecraft:dried_kelp",
                    "minecraft:tropical_fish",
                    "minecraft:apple",
                    "minecraft:chorus_fruit",
                    "minecraft:melon_slice",
                    "minecraft:beetroot",
                    "minecraft:carrot",
                    "minecraft:poisonous_potato",
                    "minecraft:potato",
                    "minecraft:beef",
                    "minecraft:chicken",
                    "minecraft:mutton",
                    "minecraft:porkchop",
                    "minecraft:rabbit",
                    "minecraft:cod"
            ), Config::validateItemName);
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_FOODS = BUILDER
            .translation("config.hangerSystemChange.ignoredFoods")
            .defineListAllowEmpty(Collections.singletonList("ignoredFoods"), List::of, Config::validateItemName);
    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static int foodQueueQuantityLength;
    public static int foodQueueCategoryLength;
    public static Float foodNutritionDecreaseByQuantity;
    public static Float foodNutritionDecreaseByCategory;
    public static Set<ResourceLocation> whitelistCanRefresh;
    public static Set<ResourceLocation> blacklist;
    public static Set<ResourceLocation> ignoredFoods;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        foodQueueQuantityLength = FOOD_QUEUE_QUANTITY_LENGTH.get();
        foodQueueCategoryLength = FOOD_QUEUE_CATEGORY_LENGTH.get();
        foodNutritionDecreaseByQuantity = FOOD_NUTRITION_DECREASE_BY_QUANTITY.get().floatValue();
        foodNutritionDecreaseByCategory = FOOD_NUTRITION_DECREASE_BY_CATEGORY.get().floatValue();
        // convert the list of strings into a set of items
        whitelistCanRefresh = WHITELIST_CAN_REFRESH.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());
        blacklist = BLACKLIST.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());
        ignoredFoods = IGNORED_FOODS.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());
    }
}
