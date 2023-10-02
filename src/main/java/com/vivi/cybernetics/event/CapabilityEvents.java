package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.capability.PlayerCyberwareProvider;
import com.vivi.cybernetics.capability.PlayerEnergyProvider;
import com.vivi.cybernetics.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModCyberware;
import net.minecraft.resources.ResourceLocation;
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
            if(!player.getCapability(ModCyberware.CYBERWARE).isPresent()) {
                event.addCapability(new ResourceLocation(Cybernetics.MOD_ID, "cyberware"), new PlayerCyberwareProvider());
            }
            if(!player.getCapability(ModCyberware.PLAYER_ENERGY).isPresent()) {
                event.addCapability(new ResourceLocation(Cybernetics.MOD_ID, "energy"), new PlayerEnergyProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClonedEvent(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(ModCyberware.CYBERWARE).ifPresent(oldStore -> {
                event.getEntity().getCapability(ModCyberware.CYBERWARE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore, true);
                });
            });
            event.getOriginal().getCapability(ModCyberware.PLAYER_ENERGY).ifPresent(oldStore -> {
                event.getEntity().getCapability(ModCyberware.PLAYER_ENERGY).ifPresent(newStore -> {
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
}
