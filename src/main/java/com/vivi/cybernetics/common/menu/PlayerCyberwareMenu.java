package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.registry.CybMenuTypes;
import net.minecraft.world.entity.player.Inventory;

public class PlayerCyberwareMenu extends CyberwareMenu {
    public PlayerCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(CybMenuTypes.PLAYER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);
        slots.forEach(slot -> {
            if(slot instanceof CyberwareSlot) {
                ((CyberwareSlot) slot).setCanEdit(false);
            }
        });

    }
}
