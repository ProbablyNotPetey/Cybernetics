package com.vivi.cybernetics.common.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.AbilityScreen;
import com.vivi.cybernetics.common.item.KineticDischargerItem;
import com.vivi.cybernetics.common.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.common.registry.CybAttributes;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.C2SDoubleJumpPacket;
import com.vivi.cybernetics.server.network.packet.C2SOpenCyberwarePacket;
import com.vivi.cybernetics.server.network.packet.C2SSpikePacket;
import com.vivi.cybernetics.server.network.packet.C2SSpikeShockwavePacket;
import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.common.registry.CybKeybinds;
import com.vivi.cybernetics.common.registry.CybMobEffects;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import com.vivi.cybernetics.client.util.InputHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    private static boolean releasedJump;
    private static boolean canDoubleJump;
    private static int currentJumps;
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
        if (InputHelper.isAbilityKeyHeld() && Minecraft.getInstance().screen == null && AbilityHelper.getAbilities(Minecraft.getInstance().player).isPresent()) {
            Minecraft.getInstance().setScreen(new AbilityScreen());
        }
        handleDoubleJump();
        handleSpike();
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if(event.getEntity().hasEffect(CybMobEffects.PARALYZED.get())) {
            event.getInput().jumping = false;
        }
    }

    private static void handleSpike() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        if(isSpiking && player.isOnGround() && !(player.isInWater()) && !player.getAbilities().flying
                && CyberwareHelper.hasCyberwareItem(player, CybItems.KINETIC_DISCHARGER.get())
                &&  !player.getCooldowns().isOnCooldown(CybItems.KINETIC_DISCHARGER.get())) {
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
            currentJumps = (int) player.getAttribute(CybAttributes.DOUBLE_JUMPS.get()).getValue();
        }
        else if(!player.input.jumping) {
            //jump key is not down, and player is in the air
            releasedJump = true;
        }
        else if(!player.getAbilities().flying && canDoubleJump && releasedJump && currentJumps > 0) {
            //jump key pressed, player can double jump and has released the jump key
            currentJumps--;
            if(currentJumps == 0) {
                canDoubleJump = false;
            }
            releasedJump = false;
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
