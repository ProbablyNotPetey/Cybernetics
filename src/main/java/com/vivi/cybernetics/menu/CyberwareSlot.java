package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.util.ToggleableSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CyberwareSlot extends ToggleableSlot {

    private Player player;
    private boolean canEdit;
    private final CyberwareInventory cyberware;
    private final int index;
    public final ResourceLocation id;

    public CyberwareSlot(CyberwareInventory cyberware, int index, int xPosition, int yPosition, Player player) {
        super(cyberware, index, xPosition, yPosition);
        isOn = false;
        this.player = player;
        this.canEdit = true;
        this.cyberware = cyberware;
        this.index = index;
        this.id = cyberware.getSectionFromSlot(index).id;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return canEdit && super.mayPickup(playerIn);
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return canEdit && super.mayPlace(stack);
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
