package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.util.ToggleableSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CyberwareMenu extends AbstractContainerMenu {

    protected Inventory inventory;
    protected CyberwareInventory cyberware;
    protected final int invX = 8;
    protected final int invY = 174;

    protected CyberwareMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(pMenuType, pContainerId);

        this.inventory = inventory;
        this.cyberware = cyberware;
        int x = 12, y = 135;

        int yOffset = 0, counter = 0;

        for(int i = 0; i < cyberware.getSlots(); i++) {
            ResourceLocation last = i > 0 ? cyberware.getSectionFromSlot(i - 1).id : null;
            if(last != null && !last.equals(cyberware.getSectionFromSlot(i).id)) {
                counter = 0;
            }
            addSlot(new CyberwareSlot(cyberware, i, x + counter * 19 - 1, y + yOffset + 1, this.inventory.player));
            counter++;
        }

        addPlayerHotbar(this.inventory);
        addPlayerInventory(this.inventory);

    }

    private void addPlayerInventory(Inventory inv) {
        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 9; c++) {
                this.addSlot(new Slot(inv, c + r*9 + 9, invX + c*18, invY + r*18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for(int c = 0; c < 9; c++) {
            this.addSlot(new Slot(inv, c, invX + c*18, invY + 58));
        }

    }



    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return cyberware.getStackInSlot(index);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.isAlive();
    }

    public CyberwareInventory getCyberware() {
        return cyberware;
    }

    public void switchActiveSlots(ResourceLocation section) {
        Cybernetics.LOGGER.info("Switching slots");
        for(int i = 0; i < cyberware.getSlots(); i++) {
            if(cyberware.getSectionFromSlot(i).id.equals(section)) {
                ((ToggleableSlot)getSlot(i)).turnOn();
            }
            else {
                ((ToggleableSlot)getSlot(i)).turnOff();
            }
        }
    }
}
