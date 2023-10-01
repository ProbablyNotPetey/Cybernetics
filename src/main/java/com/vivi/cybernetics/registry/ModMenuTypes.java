package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.menu.CyberwareStationMenu;
import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Cybernetics.MOD_ID);


    public static final RegistryObject<MenuType<CyberwareStationMenu>> CYBERWARE_STATION_MENU =
            MENU_TYPES.register("cyberware_station_menu", () -> IForgeMenuType.create(CyberwareStationMenu::new));
    public static final RegistryObject<MenuType<PlayerCyberwareMenu>> PLAYER_CYBERWARE_MENU =
            MENU_TYPES.register("player_cyberware_menu", () -> IForgeMenuType.create((pContainerId, inventory, buf) -> new PlayerCyberwareMenu(pContainerId, inventory, inventory.player.getCapability(ModCapabilities.PLAYER_CYBERWARE).orElse(null))));


    public static final RegistryObject<MenuType<SurgicalChamberCyberwareMenu>> SURGICAL_CHAMBER_CYBERWARE_MENU =
            MENU_TYPES.register("surgical_chamber_cyberware_menu", () -> IForgeMenuType.create(SurgicalChamberCyberwareMenu::new));



    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

}
