package com.vivi.cybernetics.common.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.AbilityScreen;
import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import com.vivi.cybernetics.common.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.common.registry.*;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import com.vivi.cybernetics.client.util.InputHelper;
import com.vivi.cybernetics.server.network.packet.c2s.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
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
    private static boolean canDash;
    private static boolean releasedDash;

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
        handleDash();
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

        if(isSpiking && player.onGround() && !(player.isInWater()) && !player.getAbilities().flying
                && AbilityHelper.isEnabled(player, CybAbilities.KINETIC_DISCHARGER.get())) {
            //shockwave
            CybPackets.sendToServer(new C2SSpikeShockwavePacket());
            CybPackets.sendToServer(new C2SSpikePacket());
        }

        if(player.onGround() || player.onClimbable()  || player.isInWater() || player.getAbilities().flying) {
            canSpike = false;
            isSpiking = false;
            if(AbilityHelper.isEnabled(player, CybAbilities.KINETIC_DISCHARGER.get())) {
                CybPackets.sendToServer(new C2SSpikePacket());
            }
        }
        else if(!isSpiking) {
            canSpike = true;
        }

        //todo: fix
        if(canSpike && !isSpiking && player.isShiftKeyDown() && isHighEnoughToSpike(player)) {
            canSpike = false;
            isSpiking = true;
            CybPackets.sendToServer(new C2SSpikePacket());
        }
    }

    private static boolean isHighEnoughToSpike(Player player) {
        if(player.blockPosition().getY() > player.level().getMaxBuildHeight()) {
            return player.blockPosition().getY() - player.level().getMaxBuildHeight() >= 3;
        }

        for(int i = 1; i <= 3; i++) {
            BlockPos pos = player.blockPosition().below(i);
            if(!player.level().getBlockState(pos).isAir()) return false;
        }
        return true;
    }

    private static void handleDoubleJump() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        if(player.onGround() || player.onClimbable() && !(player.isInWater())) {
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
                    if(AbilityHelper.isEnabled(player, CybAbilities.KINETIC_DISCHARGER.get())) {
                        CybPackets.sendToServer(new C2SSpikePacket());
                    }
                }
                canDash = true;
            }


        }
    }

    private static void handleDash() {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        if(player.onGround() || player.onClimbable() && !player.isInWater()) {
            canDash = true;
        }

        if(!CybKeybinds.DASH.isDown()) {
            releasedDash = true;
        }

        if(!player.getAbilities().flying && canDash && releasedDash && CybKeybinds.DASH.isDown()) {
            canDash = false;
            releasedDash = false;
            if(isSpiking && !AbilityHelper.isOnCooldown(player, CybAbilities.MK2_DASH.get())) {
                canSpike = false;
                isSpiking = false;
                if(AbilityHelper.isEnabled(player, CybAbilities.KINETIC_DISCHARGER.get())) {
                    CybPackets.sendToServer(new C2SSpikePacket());
                }
            }
            CybPackets.sendToServer(new C2SDashPacket());
        }
    }
}
