package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.common.util.ToggleableSlot;
import net.minecraftforge.items.IItemHandler;

public class InventorySlot extends ToggleableSlot {

    private final int page;
    public InventorySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, int page) {
        super(itemHandler, index, xPosition, yPosition);
        this.page = page;
        this.turnOff();
    }

    public int getPage() {
        return page;
    }
}
