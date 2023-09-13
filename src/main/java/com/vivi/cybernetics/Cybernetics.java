package com.vivi.cybernetics;

import com.mojang.logging.LogUtils;
import com.vivi.cybernetics.client.gui.CyberwareStationScreen;
import com.vivi.cybernetics.client.gui.PlayerCyberwareScreen;
import com.vivi.cybernetics.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Cybernetics.MOD_ID)
public class Cybernetics {
    public static final String MOD_ID = "cybernetics";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab TAB = new CreativeModeTab(Cybernetics.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.BARRIER);
        }
    };

    public Cybernetics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Registration
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        modEventBus.addListener(ModBlocks::registerBlockItems);

        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        //Setup

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.CYBERWARE_STATION_MENU.get(), CyberwareStationScreen::new);
        MenuScreens.register(ModMenuTypes.PLAYER_CYBERWARE_MENU.get(), PlayerCyberwareScreen::new);
        Cybernetics.LOGGER.info("Registered screens");
    }


}
