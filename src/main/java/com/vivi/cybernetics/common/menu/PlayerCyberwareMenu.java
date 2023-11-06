package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.registry.CybMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class PlayerCyberwareMenu extends CyberwareMenu{
    public PlayerCyberwareMenu(int containerId, Inventory inventory, CyberwareInventory cyberware) {
        super(CybMenuTypes.PLAYER_CYBERWARE_MENU.get(), containerId, inventory, cyberware, false);
    }
}
