package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.AbilityScreen;
import com.vivi.cybernetics.item.KineticDischargerItem;
import com.vivi.cybernetics.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.network.packet.C2SDoubleJumpPacket;
import com.vivi.cybernetics.network.packet.C2SOpenCyberwarePacket;
import com.vivi.cybernetics.network.packet.C2SSpikePacket;
import com.vivi.cybernetics.network.packet.C2SSpikeShockwavePacket;
import com.vivi.cybernetics.registry.CybItems;
import com.vivi.cybernetics.registry.CybKeybinds;
import com.vivi.cybernetics.util.CyberwareHelper;
import com.vivi.cybernetics.util.client.InputHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    private static boolean releasedJump;
    private static boolean canDoubleJump;
    private static boolean canSpike;
    private static boolean isSpiking;

    @SubscribeEvent
    public static void onRegisterKeybindignsEvent(RegisterKeyMappingsEvent event) {
        event.register(CybKeybinds.PLAYER_CYBERWARE_MENU);
        event.register(CybKeybinds.PLAYER_ABILITIES_MENU);
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        if(CybKeybinds.PLAYER_CYBERWARE_MENU.isDown()) {
            CybPackets.sendToServer(new C2SOpenCyberwarePacket());
        }
        if (InputHelper.isAbilityKeyHeld() && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().setScreen(new AbilityScreen());
        }
        handleDoubleJump();
        handleSpike();
    }

    private static void handleSpike() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        if(isSpiking && player.isOnGround() && !(player.isInWater()) && !player.getAbilities().flying) {
            //shockwave
            CybPackets.sendToServer(new C2SSpikeShockwavePacket());
        }

        if(player.isOnGround() || player.onClimbable()  || player.isInWater() || player.getAbilities().flying) {
            canSpike = false;
            isSpiking = false;
        }
        else if(!isSpiking) {
            canSpike = true;
        }

        //todo: fix
        if(canSpike && !isSpiking && player.isShiftKeyDown() && player.getDeltaMovement().y() <= -0.5f) {
            canSpike = false;
            isSpiking = true;
            if(CyberwareHelper.hasCyberwareItem(player, CybItems.KINETIC_DISCHARGER.get()) && !player.getCooldowns().isOnCooldown(CybItems.KINETIC_DISCHARGER.get())) {
                CybPackets.sendToServer(new C2SSpikePacket());
                KineticDischargerItem.spike(player);
            }
        }
    }

    private static void handleDoubleJump() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        if(player.isOnGround() || player.onClimbable() && !(player.isInWater())) {
            releasedJump = false;
            canDoubleJump = true;
        }
        else if(!player.input.jumping) {
            //jump key is not down, and player is in the air
            releasedJump = true;
        }
        else if(!player.getAbilities().flying && canDoubleJump && releasedJump) {
            //jump key pressed, player can double jump and has released the jump key
            canDoubleJump = false;
            if(CyberwareHelper.hasCyberwareItem(player, CybItems.REINFORCED_TENDONS.get())) {
                CybPackets.sendToServer(new C2SDoubleJumpPacket());
                ReinforcedTendonsItem.doubleJump(player);

                if(isSpiking) {
                    canSpike = false;
                    isSpiking = false;
                }
            }


        }
    }
}
