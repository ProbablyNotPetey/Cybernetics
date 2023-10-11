package com.vivi.cybernetics.network;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.capability.PlayerAbilities;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.data.CyberwareProperties;
import com.vivi.cybernetics.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.network.packet.S2CSyncAbilitiesPacket;
import com.vivi.cybernetics.network.packet.S2CSyncCyberwarePacket;
import com.vivi.cybernetics.network.packet.S2CSyncCyberwarePropertiesPacket;
import com.vivi.cybernetics.util.AbilityHelper;
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

    public static void handleSyncAbiilitiesPacket(NetworkEvent.Context ctx, S2CSyncAbilitiesPacket packet) {
        Player player = (Player)Minecraft.getInstance().level.getEntity(packet.getOwnerId());
        PlayerAbilities abilities = new PlayerAbilities(player);
        abilities.deserializeNBT(packet.getAbilitiesData());
        AbilityHelper.getAbilities(player).ifPresent(playerAbilities -> {
            playerAbilities.copyFrom(abilities);
        });
    }

    public static void handleSyncCyberwarePropertiesPacket(NetworkEvent.Context ctx, S2CSyncCyberwarePropertiesPacket packet) {
        CyberwarePropertiesReloadListener.INSTANCE.fromPacket(packet.getProperties());
        //apparently I DON'T have to call this on the client??? what.
//        CyberwareHelper.setupCyberwareProperties();
    }
}
