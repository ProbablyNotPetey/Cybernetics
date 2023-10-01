package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModCapabilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        player.getCapability(ModCapabilities.CYBERWARE).ifPresent(cyberware -> {
            for(int i = 0; i < cyberware.getSlots(); i++) {
                if (cyberware.getStackInSlot(i).getItem() instanceof CyberwareItem item) {
                    item.cyberwareTick(cyberware.getStackInSlot(i), player.level, player);
                }
            }
        });
    }

}
