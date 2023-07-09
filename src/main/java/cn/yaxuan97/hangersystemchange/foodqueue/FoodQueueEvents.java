package cn.yaxuan97.hangersystemchange.foodqueue;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import static cn.yaxuan97.hangersystemchange.HangerSystemChangeMod.MODID;

public class FoodQueueEvents {
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(MODID, "foodqueue"), new PlayerFoodQueueProvider());
            }
        }
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerFoodQueue.class);
    }

    public static void onPlayerCloned(PlayerEvent.Clone event) {
        // We need to copyFrom the capabilities
        event.getOriginal().getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent(oldStore -> event.getEntity().getCapability(PlayerFoodQueueProvider.PLAYER_FOOD_QUEUE_CAPABILITY).ifPresent(newStore -> newStore.copyFrom(oldStore)));
    }
}
