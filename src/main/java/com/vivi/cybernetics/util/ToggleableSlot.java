package com.vivi.cybernetics.util;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ToggleableSlot extends SlotItemHandler {
    protected boolean isOn = true;


    public ToggleableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);

    }

    public ToggleableSlot turnOn() {
        isOn = true;
        return this;
    }

    public ToggleableSlot turnOff() {
        isOn = false;
        return this;
    }

    @Override
    public boolean isActive() {
        return isOn && super.isActive();
    }
}
