package com.vivi.cybernetics.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return isOn && super.mayPlace(stack);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return isOn && super.mayPickup(playerIn);
    }
}
