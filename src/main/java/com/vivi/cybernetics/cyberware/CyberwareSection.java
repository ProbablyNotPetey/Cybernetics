package com.vivi.cybernetics.cyberware;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareSection extends ItemStackHandler {

    public final ResourceLocation id;
    public CyberwareSection(ResourceLocation id, int size) {
        super(size);
        this.id = id;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack);
    }
}
