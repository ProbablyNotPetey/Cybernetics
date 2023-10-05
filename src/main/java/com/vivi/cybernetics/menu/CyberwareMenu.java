package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
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
    protected final int invY = 158;

    protected CyberwareMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(pMenuType, pContainerId);

        this.inventory = inventory;
        this.cyberware = cyberware;
        int x = 12, y = 135;

        int yOffset = 0, counter = 0;

        for(int i = 0; i < cyberware.getSlots(); i++) {
            CyberwareSectionType last = i > 0 ? cyberware.getSectionFromSlot(i - 1).getType() : null;
            if(last != null && !last.equals(cyberware.getSectionFromSlot(i).getType())) {
                counter = 0;
            }
            addSlot(new CyberwareSlot(cyberware, i, x + counter * 19 - 1, y + yOffset + 1, this.inventory.player));
            counter++;
        }

        addPlayerInventory(this.inventory);
        addPlayerHotbar(this.inventory);

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


    /**
     * Mostly stolen from {@link net.minecraft.world.inventory.InventoryMenu#quickMoveStack(Player, int)}.
     * <p>
     * Moves cyberware into inventory, you cannot shift click inventory items into cyberware
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stackOut = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        //clicked on item
        if(slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stackOut = stackInSlot.copy();
            //clicked on cyberware slot
            if(index < cyberware.getSlots()) {
                if(!this.moveItemStackTo(stackInSlot, cyberware.getSlots(), this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            //clicked on inventory slot
            else if(index >= cyberware.getSlots() && index < this.slots.size() - 9) {
                if(!this.moveItemStackTo(stackInSlot, this.slots.size() - 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            //clicked on hotbar slot
            else if(index >= cyberware.getSlots() - 9 && index < this.slots.size()) {
                if(!this.moveItemStackTo(stackInSlot, cyberware.getSlots(), this.slots.size() - 9, false)) {
                    return ItemStack.EMPTY;
                }
            }
            //failsafe
            else if(!this.moveItemStackTo(stackInSlot, cyberware.getSlots(), this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }

            //idk below this line lol
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == stackOut.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }


        return stackOut;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.isAlive();
    }

    public CyberwareInventory getCyberware() {
        return cyberware;
    }

    public void switchActiveSlots(CyberwareSectionType section) {
        for(int i = 0; i < cyberware.getSlots(); i++) {
            if(cyberware.getSectionFromSlot(i).getType().equals(section)) {
                ((ToggleableSlot)getSlot(i)).turnOn();
            }
            else {
                ((ToggleableSlot)getSlot(i)).turnOff();
            }
        }
    }
}
