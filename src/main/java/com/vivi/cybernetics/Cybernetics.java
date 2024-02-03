package com.vivi.cybernetics;

import com.mojang.logging.LogUtils;
import com.vivi.cybernetics.client.gui.CyberwareStationScreen;
import com.vivi.cybernetics.client.gui.cyberware.CyberwareScreen;
import com.vivi.cybernetics.client.hud.AbilityHUD;
import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import com.vivi.cybernetics.client.hud.MobEffectHUD;
import com.vivi.cybernetics.client.particle.BlastWaveParticleType;
import com.vivi.cybernetics.client.particle.FallingParticleType;
import com.vivi.cybernetics.client.shader.CybCoreShaders;
import com.vivi.cybernetics.client.shader.CybPostShaders;
import com.vivi.cybernetics.client.util.HudAnchor;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.common.config.ClientConfig;
import com.vivi.cybernetics.common.config.CommonConfig;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.common.menu.SurgicalChamberCyberwareMenu;
import com.vivi.cybernetics.common.registry.*;
import com.vivi.cybernetics.server.network.CybPackets;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Cybernetics.MOD_ID)
public class Cybernetics {
    public static final String MOD_ID = "cybernetics";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Capability<CyberwareInventory> CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });
    public static final Capability<PlayerAbilities> PLAYER_ABILITIES = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<PlayerEnergyStorage> PLAYER_ENERGY = CapabilityManager.get(new CapabilityToken<>() { });




    public Cybernetics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Registration
        CybBlocks.register(modEventBus);
        CybItems.register(modEventBus);
        CybCreativeTabs.register(modEventBus);
        modEventBus.addListener(CybBlocks::registerBlockItems);
        CybMenuTypes.register(modEventBus);
        CybRecipeTypes.register(modEventBus);
        CybCyberware.register(modEventBus);
        CybAbilities.register(modEventBus);
        CybMobEffects.register(modEventBus);
        CybAttributes.register(modEventBus);
        CybParticles.register(modEventBus);

        //Setup
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        // Register ourselves for mod event bus
        modEventBus.register(this);

    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        CybPackets.register();
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(CybMenuTypes.CYBERWARE_STATION_MENU.get(), CyberwareStationScreen::new);

        MenuScreens.register(CybMenuTypes.PLAYER_CYBERWARE_MENU.get(), CyberwareScreen<PlayerCyberwareMenu>::new);
        MenuScreens.register(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), CyberwareScreen<SurgicalChamberCyberwareMenu>::new);

        CybCoreShaders.getInstance().init();
        CybPostShaders.getInstance().init();
        CyberneticsHUD.getInstance().addHUDElement(new AbilityHUD(HudAnchor.TOP_RIGHT, -162, 4));
        CyberneticsHUD.getInstance().addHUDElement(new MobEffectHUD(HudAnchor.MIDDLE_LEFT, 4, -79));
    }

    @SubscribeEvent
    public void registerKeybindings(RegisterKeyMappingsEvent event) {
        event.register(CybKeybinds.PLAYER_CYBERWARE_MENU);
        event.register(CybKeybinds.PLAYER_ABILITIES_MENU);
        event.register(CybKeybinds.DASH);
    }

    @SubscribeEvent
    public void registerHUD(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "cybernetics_hud", CyberneticsHUD.getInstance());
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CyberwareInventory.class);
        event.register(PlayerEnergyStorage.class);
        event.register(PlayerAbilities.class);
    }

    @SubscribeEvent
    public void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpecial(CybParticles.BLAST_WAVE.get(), new BlastWaveParticleType.Provider());
        event.registerSpecial(CybParticles.FALLING_PARTICLE.get(), new FallingParticleType.Provider());
    }


}
