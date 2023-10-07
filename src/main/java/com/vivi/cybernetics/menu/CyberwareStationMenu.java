package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.block.entity.CyberwareStationBlockEntity;
import com.vivi.cybernetics.registry.CybBlocks;
import com.vivi.cybernetics.registry.CybMenuTypes;
import com.vivi.cybernetics.util.ModEnergyStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;

public class CyberwareStationMenu extends AbstractContainerMenu {

    public final CyberwareStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private final int invX = 8;
    private final int invY = 92;

    public CyberwareStationMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, inv.player.level.getBlockEntity(buf.readBlockPos()), new SimpleContainerData(2));
    }

    public CyberwareStationMenu(int id, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(CybMenuTypes.CYBERWARE_STATION_MENU.get(), id);
        checkContainerSize(inv, 8);
        checkContainerDataCount(data, 2);
        this.blockEntity = (CyberwareStationBlockEntity) blockEntity;
        this.level = inv.player.level;
        this.data = data;



        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 26,  39));
            this.addSlot(new SlotItemHandler(handler, 1, 62,  21));
            this.addSlot(new SlotItemHandler(handler, 2, 80,  21));
            this.addSlot(new SlotItemHandler(handler, 3, 98,  21));
            this.addSlot(new SlotItemHandler(handler, 4, 62,  39));
            this.addSlot(new SlotItemHandler(handler, 5, 80,  39));
            this.addSlot(new SlotItemHandler(handler, 6, 98,  39));
            this.addSlot(new SlotItemHandler(handler, 7, 148,  56));
        });

        //add inventory
        addPlayerHotbar(inv);
        addPlayerInventory(inv);

        addDataSlots(data);
        //data slots are 16 bit so set up manually here
        trackPower();
    }

    //i am not going to pretend i know how bitwise operators work i just copied mcjty LOL
    private void trackPower() {
        //lower 16 bits stored energy
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getStoredEnergy() & 0xffff;
            }

            @Override
            public void set(int value) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                    int energyStored = energy.getEnergyStored() & 0xffff0000;
                    ((ModEnergyStorage)energy).setEnergy(energyStored + (value & 0xffff));
                });
            }
        });
        //higher 16 bits stored energy
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getStoredEnergy() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                    int energyStored = energy.getEnergyStored() & 0x0000ffff;
                    ((ModEnergyStorage)energy).setEnergy(energyStored | (value << 16));
                });
            }
        });
        //lower 16 bits max energy
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getMaxEnergy() & 0xffff;
            }

            @Override
            public void set(int value) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                    int energyStored = energy.getMaxEnergyStored() & 0xffff0000;
                    ((ModEnergyStorage)energy).setMaxEnergy(energyStored + (value & 0xffff));
                });
            }
        });
        //higher 16 bits max energy
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getMaxEnergy() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                blockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
                    int energyStored = energy.getMaxEnergyStored() & 0x0000ffff;
                    ((ModEnergyStorage)energy).setMaxEnergy(energyStored | (value << 16));
                });
            }
        });
    }

    public int getStoredEnergy() {
        return blockEntity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getMaxEnergy() {
        return blockEntity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, CybBlocks.CYBERWARE_STATION_BLOCK.get());
    }

    //is progress > 0
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int progressArrowSize = 105; //change this

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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



    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 8;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
