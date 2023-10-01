package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class PlayerCyberwareMenu extends CyberwareMenu {
    public PlayerCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(ModMenuTypes.PLAYER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);
        slots.forEach(slot -> {
            if(slot instanceof CyberwareSlot) {
//                ((CyberwareSlot) slot).setCanEdit(false);
            }
        });

        Cybernetics.LOGGER.info("Created player cyberware. Client: " + inventory.player.level.isClientSide);
    }
}
