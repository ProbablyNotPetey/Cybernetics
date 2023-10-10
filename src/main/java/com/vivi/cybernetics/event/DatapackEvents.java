package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
