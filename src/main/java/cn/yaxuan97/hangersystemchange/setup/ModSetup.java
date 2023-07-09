package cn.yaxuan97.hangersystemchange.setup;

import cn.yaxuan97.hangersystemchange.foodqueue.FoodQueueEvents;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


import static cn.yaxuan97.hangersystemchange.HangerSystemChangeMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    public static void setup() {
        var bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(Entity.class, FoodQueueEvents::onAttachCapabilitiesPlayer);
        bus.addListener(FoodQueueEvents::onRegisterCapabilities);
        bus.addListener(FoodQueueEvents::onPlayerCloned);
    }
    public static void init(FMLCommonSetupEvent ignoredEvent) {
        Message.register();
    }
}
