package com.vivi.cybernetics.common.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.server.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class DatapackEvents {


    @SubscribeEvent
    public static void onAddReloadListenersEvent(AddReloadListenerEvent event) {
        event.addListener(CyberwarePropertiesReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public static void onDatapackSyncEvent(OnDatapackSyncEvent event) {
        if(event.getPlayer() != null) {
            CyberwarePropertiesReloadListener.INSTANCE.syncProperties(event.getPlayer());
        }
        else {
            event.getPlayerList().getPlayers().forEach(CyberwarePropertiesReloadListener.INSTANCE::syncProperties);
        }
        CyberwareHelper.setupCyberwareProperties();
    }
}
