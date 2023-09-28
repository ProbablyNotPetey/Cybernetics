package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SurgicalChamberCyberwareScreen extends CyberwareScreen<SurgicalChamberCyberwareMenu> {
    public SurgicalChamberCyberwareScreen(SurgicalChamberCyberwareMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
}
