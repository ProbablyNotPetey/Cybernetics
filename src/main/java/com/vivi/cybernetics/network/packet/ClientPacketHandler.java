package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class ClientPacketHandler {

    public static void handleDoubleJumpPacket(NetworkEvent.Context ctx) {
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            ReinforcedTendonsItem.doubleJump(player);
            //play double jump sound
        }
    }

    public static void handleSyncCyberwarePacket(NetworkEvent.Context ctx, S2CSyncCyberwarePacket packet) {

        Player player = (Player)Minecraft.getInstance().level.getEntity(packet.getOwnerId());
        CyberwareInventory inventory = CyberwareInventory.create(player);
        inventory.deserializeNBT(packet.getCyberwareData());
        CyberwareHelper.getCyberware(player).ifPresent(cyberware -> {
            cyberware.copyFrom(inventory, false);
        });


    }
}
