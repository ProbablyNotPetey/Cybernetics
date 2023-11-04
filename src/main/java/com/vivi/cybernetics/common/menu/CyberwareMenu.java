package com.vivi.cybernetics.common.menu;

import com.mojang.datafixers.util.Pair;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.common.item.AttributeCyberwareItem;
import com.vivi.cybernetics.common.registry.CybAttributes;
import com.vivi.cybernetics.common.util.ToggleableSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CyberwareMenu extends AbstractContainerMenu {

    protected Inventory inventory;
    protected CyberwareInventory cyberware;
    protected CyberwareSectionType activeSection;
    protected final int invX = 24;
    protected final int invY = 157;
    protected final DataSlot capacityData;
    protected final DataSlot maxCapacityData;

    protected CyberwareMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(pMenuType, pContainerId);

        this.inventory = inventory;
        this.cyberware = cyberware;
        int x = 10, y = 30;

        int counter = 0, rows = 4;

        for(int i = 0; i < cyberware.getSlots(); i++) {
            CyberwareSectionType last = i > 0 ? cyberware.getSectionFromSlot(i - 1).getType() : null;
            if(last != null && !last.equals(cyberware.getSectionFromSlot(i).getType())) {
                counter = 0;
            }
            addSlot(new CyberwareSlot(cyberware, i, x + ((counter % rows) * 25) - 1, y + ((counter / rows) * 23) + 1, this.inventory.player));
            counter++;
        }

        addPlayerInventory(this.inventory);
        addPlayerHotbar(this.inventory);
        capacityData = new DataSlot() {
            @Override
            public int get() {
                return cyberware.getStoredCapacity();
            }

            @Override
            public void set(int pValue) {

            }
        };
        addDataSlot(capacityData);
        maxCapacityData = new DataSlot() {
            private int maxCapacity = (int) inventory.player.getAttribute(CybAttributes.MAX_CAPACITY.get()).getValue();

            @Override
            public int get() {
                return maxCapacity;
            }

            @Override
            public void set(int pValue) {
                maxCapacity = pValue;
            }
        };
        addDataSlot(maxCapacityData);
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

    public int getStoredCapacity() {
        return capacityData.get();
    }

    public int getMaxCapacity() {
        return maxCapacityData.get();
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
        this.activeSection = section;
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
