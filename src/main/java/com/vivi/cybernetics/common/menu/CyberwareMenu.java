package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.registry.CybAttributes;
import com.vivi.cybernetics.common.util.ToggleableSlot;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberwareMenu extends AbstractContainerMenu {

    //Note: this opens up for the possibility of a dupe bug, if some mod removes items from the player's inventory before they confirm.
    protected final IItemHandlerModifiable inventory;
    protected final CyberwareInventory cyberware;
    private CyberwareSectionType activeSection;
    private final boolean isClient;

    protected final NonNullList<ItemStack> stacksToAdd = NonNullList.create();
    protected final NonNullList<ItemStack> stacksToRemove = NonNullList.create();

    protected final DataSlot capacityData;
    protected final DataSlot maxCapacityData;

    private int inventorySlotId;

    private final MenuType<?> menuType;
    public CyberwareMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, CyberwareInventory cyberware) {
        super(menuType, containerId);
        this.menuType = menuType;
        this.isClient = inventory.player.level.isClientSide;
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
            addSlot(new CyberwareSlot(cyberware, i, slotX + ((counter % rows) * 25) - 1, slotY + ((counter / rows) * 23) + 1, inventory.player));
            counter++;
        }
        maxCols = Mth.ceil((float) maxCols / rows);

        inventorySlotId = slots.size();
        int invX = 10, invY = slotY + (maxCols * 23) + 5;
        for(int i = 0; i < this.inventory.getSlots(); i++) {
            addSlot(new SlotItemHandler(this.inventory, i, invX + ((i % rows) * 18) - 1, invY + ((i / rows) * 18) + 1));
        }


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
        maxCapacityData = new DataSlot() {;

            @Override
            public int get() {
                return cyberware.getMaxCapacity();
            }

            @Override
            public void set(int pValue) {

            }
        };
        addDataSlot(maxCapacityData);
    }

    public CyberwareInventory getCyberware() {
        return cyberware;
    }

    public int getStoredCapacity() {
        return capacityData.get();
    }

    public int getMaxCapacity() {
        return maxCapacityData.get();
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

    private void doClick(int slotId, int button, ClickType clickType, Player player) {

        if(slotId < 0) {
            if(!getCarried().isEmpty()) {
                player.drop(getCarried(), true);
            }
            return;
        }
        if(clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) {
            ItemStack stack = getSlot(slotId).getItem().copy();
            if(!(stack.getItem() instanceof CyberwareItem)) return;
            if(getSlot(slotId) instanceof CyberwareSlot) {
                for(int i = 0; i < inventorySlotId; i++) {
                    if(!(getSlot(i).getItem().getItem() instanceof CyberwareItem item)) {
                        continue;
                    }
                    List<Ingredient> requirements = item.getRequirements();
                    for(Ingredient req : requirements) {
                        if(req.test(stack)) {
                            //gui events etc.
                            //todo: refactor gui events to use forge events?
                            return;
                        }
                    }
                }
                if(moveItemStackTo(getSlot(slotId).getItem(), inventorySlotId, slots.size(), false)) {

                    cyberware.onContentsChanged(slotId);
                    for (ItemStack addStack : stacksToRemove) {
                        if (addStack.equals(stack, false)) {
                            stacksToRemove.remove(addStack);
                            break;
                        }
                    }
                    stacksToAdd.add(stack);
                }
            }
            else if(moveItemStackTo(getSlot(slotId).getItem(), 0, inventorySlotId, false)) {
                for (ItemStack addStack : stacksToAdd) {
                    if (addStack.equals(stack, false)) {
                        stacksToAdd.remove(addStack);
                        break;
                    }
                }
                stacksToRemove.add(stack);
            }

            Cybernetics.LOGGER.info("Client: " + isClient + ", stacksToAdd: " + stacksToAdd + ", stacksToRemove: " + stacksToRemove);
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        Inventory inventory = player.getInventory();
        stacksToAdd.forEach(stack -> {
            if(!inventory.add(stack)) {
                player.drop(stack, true);
            }
        });
        stacksToRemove.forEach(stack -> {
            for(int i = 35; i >= 0; i--) {
                if(inventory.getItem(i).equals(stack, false)) {
                    inventory.removeItem(i, inventory.getItem(i).getCount());
                    return;
                }
            }
        });
    }
}
