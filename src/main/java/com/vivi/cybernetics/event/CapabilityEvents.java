package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.capability.PlayerCyberwareProvider;
import com.vivi.cybernetics.capability.PlayerEnergyProvider;
import com.vivi.cybernetics.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.network.packet.S2CSyncCyberwarePacket;
import com.vivi.cybernetics.registry.CybCyberware;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class CapabilityEvents {

    @SubscribeEvent
    public static <T> void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player) {
            if(!player.getCapability(CybCyberware.CYBERWARE).isPresent()) {
                event.addCapability(new ResourceLocation(Cybernetics.MOD_ID, "cyberware"), new PlayerCyberwareProvider(player));
            }
            if(!player.getCapability(CybCyberware.PLAYER_ENERGY).isPresent()) {
                event.addCapability(new ResourceLocation(Cybernetics.MOD_ID, "energy"), new PlayerEnergyProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClonedEvent(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(CybCyberware.CYBERWARE).ifPresent(oldStore -> {
                event.getEntity().getCapability(CybCyberware.CYBERWARE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore, true);
                });
            });
            event.getOriginal().getCapability(CybCyberware.PLAYER_ENERGY).ifPresent(oldStore -> {
                event.getEntity().getCapability(CybCyberware.PLAYER_ENERGY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });

        }
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        event.register(CyberwareInventory.class);
        event.register(PlayerEnergyStorage.class);
    }


    @SubscribeEvent
    public static void onStartTrackingPlayer(PlayerEvent.StartTracking event) {
        if(event.getEntity().level.isClientSide) return;
        Entity target = event.getTarget();
        if(target instanceof Player player) {
            CyberwareInventory cyberware = CyberwareHelper.getCyberware(player).orElse(null);
            if(cyberware == null) return;
            CybPackets.sendToClient(new S2CSyncCyberwarePacket(player, cyberware), (ServerPlayer) target);
        }
    }

    @SubscribeEvent
    public static void onLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if(player.level.isClientSide) return;
        CyberwareInventory cyberware = CyberwareHelper.getCyberware(player).orElse(null);
        if(cyberware == null) return;
        CybPackets.sendToClient(new S2CSyncCyberwarePacket(player, cyberware), (ServerPlayer) player);
    }

    @SubscribeEvent
    public static void onChangeDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if(player.level.isClientSide) return;
        CyberwareInventory cyberware = CyberwareHelper.getCyberware(player).orElse(null);
        if(cyberware == null) return;
        CybPackets.sendToClient(new S2CSyncCyberwarePacket(player, cyberware), (ServerPlayer) player);
    }
}
