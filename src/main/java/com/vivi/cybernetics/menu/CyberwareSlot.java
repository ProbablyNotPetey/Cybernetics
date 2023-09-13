package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.util.ToggleableSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareSlot extends ToggleableSlot {

    private Player player;
    private boolean canEdit;

    public CyberwareSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player, boolean canEdit) {
        super(itemHandler, index, xPosition, yPosition);
        isOn = false;
        this.player = player;
        this.canEdit = canEdit;
    }

    public CyberwareSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player) {
        this(itemHandler, index, xPosition, yPosition, player, true);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return canEdit && super.mayPickup(playerIn);
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return canEdit && super.mayPlace(stack);
    }
}
