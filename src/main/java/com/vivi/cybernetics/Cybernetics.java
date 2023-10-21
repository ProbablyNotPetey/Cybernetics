package com.vivi.cybernetics;

import com.mojang.logging.LogUtils;
import com.vivi.cybernetics.capability.PlayerAbilities;
import com.vivi.cybernetics.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.client.gui.CyberwareScreen;
import com.vivi.cybernetics.client.gui.CyberwareStationScreen;
import com.vivi.cybernetics.client.gui.PlayerCyberwareScreenOld;
import com.vivi.cybernetics.client.gui.SurgicalChamberCyberwareScreenOld;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(Cybernetics.MOD_ID)
public class Cybernetics {
    public static final String MOD_ID = "cybernetics";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Capability<CyberwareInventory> CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });
    public static final Capability<PlayerAbilities> PLAYER_ABILITIES = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<PlayerEnergyStorage> PLAYER_ENERGY = CapabilityManager.get(new CapabilityToken<>() { });



    public static final CreativeModeTab TAB = new CreativeModeTab(Cybernetics.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.BARRIER);
        }
    };

    public Cybernetics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Registration
        CybBlocks.register(modEventBus);
        CybItems.register(modEventBus);
        modEventBus.addListener(CybBlocks::registerBlockItems);
        CybMenuTypes.register(modEventBus);
        CybRecipeTypes.register(modEventBus);
        CybCyberware.register(modEventBus);
        CybAbilities.register(modEventBus);
        CybMobEffects.register(modEventBus);
        CybAttributes.register(modEventBus);

        //Setup

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CybPackets.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(CybMenuTypes.CYBERWARE_STATION_MENU.get(), CyberwareStationScreen::new);
//        MenuScreens.register(CybMenuTypes.PLAYER_CYBERWARE_MENU.get(), PlayerCyberwareScreenOld::new);
//        MenuScreens.register(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), SurgicalChamberCyberwareScreenOld::new);

        MenuScreens.register(CybMenuTypes.PLAYER_CYBERWARE_MENU.get(), CyberwareScreen<PlayerCyberwareMenu>::new);
        MenuScreens.register(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), CyberwareScreen<SurgicalChamberCyberwareMenu>::new);

    }


}
