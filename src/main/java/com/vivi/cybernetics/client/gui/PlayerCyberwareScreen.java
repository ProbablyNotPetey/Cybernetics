package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PlayerCyberwareScreen extends CyberwareScreen<PlayerCyberwareMenu>{
    public PlayerCyberwareScreen(PlayerCyberwareMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


}
