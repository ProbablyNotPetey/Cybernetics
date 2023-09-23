package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SurgicalChamberCyberwareMenu extends CyberwareMenu {
    public SurgicalChamberCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(ModMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware, true);
    }
}
