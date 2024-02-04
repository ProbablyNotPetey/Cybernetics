package com.vivi.cybernetics.server.network;

import com.vivi.cybernetics.client.hud.AbilityHUD;
import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import com.vivi.cybernetics.client.shader.BerserkRenderer;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import com.vivi.cybernetics.server.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.server.network.packet.s2c.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import team.lodestar.lodestone.systems.easing.Easing;

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
            cyberware.copyFrom(inventory, player, packet.shouldUpdate());
        });
    }

    public static void handleSyncAbiilitiesPacket(NetworkEvent.Context ctx, S2CSyncAbilitiesPacket packet) {
        Player player = (Player)Minecraft.getInstance().level.getEntity(packet.getOwnerId());
        PlayerAbilities abilities = new PlayerAbilities(player);
        abilities.deserializeNBT(packet.getAbilitiesData());
        AbilityHelper.getAbilities(player).ifPresent(playerAbilities -> {
            playerAbilities.copyFrom(abilities);
            packet.getAbilitiesToEnable().forEach(ability -> {
                playerAbilities.enableAbility(CybAbilities.ABILITY_TYPE_REGISTRY.get().getValue(ability), false);
            });
            packet.getAbilitiesToDisable().forEach(ability -> {
                playerAbilities.disableAbility(CybAbilities.ABILITY_TYPE_REGISTRY.get().getValue(ability), false);
            });
        });

        CyberneticsHUD.getInstance().getElements().forEach(element -> {
            if(element instanceof AbilityHUD abilityHUD) {
                abilityHUD.updateElementList();
            }
        });
    }

    public static void handleSyncCyberwarePropertiesPacket(NetworkEvent.Context ctx, S2CSyncCyberwarePropertiesPacket packet) {
        CyberwarePropertiesReloadListener.INSTANCE.fromPacket(packet.getProperties());
        //apparently I DON'T have to call this on the client??? what.
//        CyberwareHelper.setupCyberwareProperties();
    }

    public static void handleHUDPacket(NetworkEvent.Context ctx, S2CToggleHUDPacket packet) {

        if(packet.isOn()) {
            CyberneticsHUD.getInstance().enable();
        }
        else {
            CyberneticsHUD.getInstance().disable();
        }
    }

    public static void handleBerserkPacket(NetworkEvent.Context ctx, S2CToggleBerserkPacket packet) {
        if(packet.isOn()) {
            BerserkRenderer.getInstance().start(0, Easing.LINEAR);
        }
        else {
            BerserkRenderer.getInstance().stop(0, Easing.LINEAR);
        }
    }
}
