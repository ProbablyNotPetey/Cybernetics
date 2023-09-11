package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.util.ToggleableSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareSlot extends ToggleableSlot {

    private Player player;

    public CyberwareSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player) {
        super(itemHandler, index, xPosition, yPosition);
        isOn = false;
        this.player = player;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return super.mayPickup(playerIn);
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return super.mayPlace(stack);
    }
}
