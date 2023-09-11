package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderTooltipColorEvent(RenderTooltipEvent.Color event) {

    }
}
