package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.common.menu.SurgicalChamberCyberwareMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SurgicalChamberCyberwareScreenOld extends CyberwareScreenOld<SurgicalChamberCyberwareMenu> {
    public SurgicalChamberCyberwareScreenOld(SurgicalChamberCyberwareMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
}
