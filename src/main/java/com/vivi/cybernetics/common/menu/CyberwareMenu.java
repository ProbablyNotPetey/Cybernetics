package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.util.ToggleableSlot;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CyberwareMenu extends AbstractContainerMenu {

    protected final IItemHandlerModifiable inventory;
    protected final CyberwareInventory cyberware;
    private int capacity;
    private int maxCapacity;
    private CyberwareSectionType activeSection;

    private final MenuType<?> menuType;
    public CyberwareMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, CyberwareInventory cyberware) {
        super(menuType, containerId);
        this.menuType = menuType;
        int counter = 0;
        this.inventory = new ItemStackHandler(Inventory.INVENTORY_SIZE);
        for(int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            if(inventory.getItem(i).getItem() instanceof CyberwareItem) {
                this.inventory.insertItem(counter++, inventory.getItem(i), false);
            }
        }
        this.cyberware = cyberware;
        int slotX = 10, slotY = 30;

        counter = 0;
        int rows = 4;
        int maxCols = 0;
        for(int i = 0; i < cyberware.getSlots(); i++) {
            CyberwareSectionType last = i > 0 ? cyberware.getSectionFromSlot(i - 1).getType() : null;
            if(last != null && !last.equals(cyberware.getSectionFromSlot(i).getType())) {
                if(counter > maxCols) {
                    maxCols = counter;
                }
                counter = 0;
            }
            addSlot(new CyberwareSlot(cyberware, i, slotX + ((counter % rows) * 25) - 1, slotY + ((counter / rows) * 23) + 1, inventory.player) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
                @Override
                public boolean mayPickup(Player playerIn) {
                    return false;
                }
            });
            counter++;
        }

        int invX = 10, invY = slotY + (maxCols * 23) + 5;
        for(int i = 0; i < this.inventory.getSlots(); i++) {
            addSlot(new ToggleableSlot(this.inventory, i, invX + ((i % rows) * 18) - 1, invY + ((i / rows) * 18) + 1) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
                @Override
                public boolean mayPickup(Player playerIn) {
                    return false;
                }
            });
        }
    }

    public CyberwareInventory getCyberware() {
        return cyberware;
    }

    public int getStoredCapacity() {
        return 0;
    }

    public int getMaxCapacity() {
        return 50;
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

    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        try {
            this.doClick(pSlotId, pButton, pClickType, pPlayer);
        } catch (Exception exception) {
            CrashReport crashreport = CrashReport.forThrowable(exception, "Container click");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Click info");
            crashreportcategory.setDetail("Menu Type", () -> this.menuType != null ? ForgeRegistries.MENU_TYPES.getKey(this.menuType).toString() : "<no type>");
            crashreportcategory.setDetail("Menu Class", () -> this.getClass().getCanonicalName());
            crashreportcategory.setDetail("Slot Count", this.slots.size());
            crashreportcategory.setDetail("Slot", pSlotId);
            crashreportcategory.setDetail("Button", pButton);
            crashreportcategory.setDetail("Type", pClickType);
            throw new ReportedException(crashreport);
        }
    }

    private void doClick(int slot, int button, ClickType clickType, Player player) {
        if(clickType == ClickType.PICKUP) {

        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int index) {
        return slots.get(index).getItem();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.isAlive();
    }
}
