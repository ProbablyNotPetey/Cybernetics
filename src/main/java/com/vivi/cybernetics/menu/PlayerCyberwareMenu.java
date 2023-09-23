package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class PlayerCyberwareMenu extends CyberwareMenu {
    public PlayerCyberwareMenu(int containerId, Inventory inventory, CyberwareInventory cyberware) {
        super(ModMenuTypes.PLAYER_CYBERWARE_MENU.get(), containerId, inventory, cyberware, false);
    }
}
