package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.network.PacketHandler;
import com.vivi.cybernetics.network.packet.C2SOpenCyberwarePacket;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModKeybinds;
import com.vivi.cybernetics.registry.ModTags;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterKeybindignsEvent(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.PLAYER_CYBERWARE_MENU);
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        if(ModKeybinds.PLAYER_CYBERWARE_MENU.isDown()) {
            PacketHandler.sendToServer(new C2SOpenCyberwarePacket());
        }
    }
}
