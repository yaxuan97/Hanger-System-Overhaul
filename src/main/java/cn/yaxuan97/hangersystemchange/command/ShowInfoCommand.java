package cn.yaxuan97.hangersystemchange.command;

import cn.yaxuan97.hangersystemchange.Config;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

public class ShowInfoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("showinfo").executes((CommandContext) -> {
            Component component = ComponentUtils.fromMessage(() -> "blacklist:\n" + Config.blacklist.toString() +
                    "\nwhitelist:\n" + Config.whitelistCanRefresh.toString() +
                    "\nignore:\n" + Config.ignoredFoods.toString() +
                    "\nfoodQueueCategoryLength: " + Config.foodQueueCategoryLength +
                    "\nfoodQueueQuantityLength: " + Config.foodQueueQuantityLength +
                    "\nfoodNutritionDecreaseByCategory: " + Config.foodNutritionDecreaseByCategory +
                    "\nfoodNutritionDecreaseByQuantity: " + Config.foodNutritionDecreaseByQuantity);
            CommandContext.getSource().sendSuccess(() -> Component.translatable("commands.showinfo.success", component), false);
            return 0;
        }));
    }
}
