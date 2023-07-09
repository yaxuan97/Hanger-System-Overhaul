package cn.yaxuan97.hangersystemchange.setup;

import cn.yaxuan97.hangersystemchange.command.ReloadFoodQueueCommand;
import cn.yaxuan97.hangersystemchange.command.ShowInfoCommand;
import cn.yaxuan97.hangersystemchange.foodqueue.PlayerFoodQueueProvider;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.yaxuan97.hangersystemchange.HangerSystemChangeMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventListener {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ShowInfoCommand.register(event.getDispatcher());
        ReloadFoodQueueCommand.register(event.getDispatcher());
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent(cap -> cap.sync(event.getEntity()));
    }
}
