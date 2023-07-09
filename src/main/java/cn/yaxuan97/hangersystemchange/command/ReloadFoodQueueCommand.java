package cn.yaxuan97.hangersystemchange.command;

import cn.yaxuan97.hangersystemchange.foodqueue.PlayerFoodQueueProvider;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadFoodQueueCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var literalcommandnode = dispatcher.register(Commands.literal("reloadfoodqueue").executes((CommandContext) -> {
            var player = CommandContext.getSource().getPlayerOrException();
            player.getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent((playerFoodQueue -> playerFoodQueue.sync(player)));
            CommandContext.getSource().sendSuccess(() -> Component.translatable("commands.reloadfoodqueue.success"), true);
            return 0;
        }));
        dispatcher.register(Commands.literal("rlfq").redirect(literalcommandnode));
    }
}
