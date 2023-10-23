package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.common.menu.PlayerCyberwareMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PlayerCyberwareScreenOld extends CyberwareScreenOld<PlayerCyberwareMenu> {
    public PlayerCyberwareScreenOld(PlayerCyberwareMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


}
