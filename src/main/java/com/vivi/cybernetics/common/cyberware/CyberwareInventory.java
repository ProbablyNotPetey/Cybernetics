package com.vivi.cybernetics.common.cyberware;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.item.CapacityCyberwareItem;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.S2CSyncAbilitiesPacket;
import com.vivi.cybernetics.server.network.packet.S2CSyncCyberwarePacket;
import com.vivi.cybernetics.common.registry.CybCyberware;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CyberwareInventory extends CombinedInvWrapper implements INBTSerializable<CompoundTag> {

    private final Player owner;
    private int capacity;
    private int maxCapacity;
    public CyberwareInventory(Player owner, IItemHandlerModifiable... itemHandler) {
        super(itemHandler);
        this.owner = owner;
        this.maxCapacity = 50;
    }



    public static CyberwareInventory create(Player owner) {

        List<CyberwareSection> sections = new ArrayList<>();
        CybCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getEntries().forEach(type -> {
            sections.add(new CyberwareSection(type.getValue(), type.getKey().location()));
        });

        return new CyberwareInventory(owner, sections.toArray(new IItemHandlerModifiable[0]));
    }
    public void copyFrom(CyberwareInventory other) {
        copyFrom(other, null, false);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        super.setStackInSlot(slot, stack);
        onContentsChanged(slot);
//        initCapacity();
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack out = super.extractItem(slot, amount, simulate);
        if(!simulate) {
            onContentsChanged(slot);
//            if(getStackInSlot(slot).getItem() instanceof CyberwareItem) {
//                capacity -= ((CyberwareItem) getStackInSlot(slot).getItem()).getCapacity();
//            }
        }
        return out;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack out =  super.insertItem(slot, stack, simulate);
        if(!simulate) {
            onContentsChanged(slot);
//            if(getStackInSlot(slot).getItem() instanceof CyberwareItem) {
//                capacity += ((CyberwareItem) getStackInSlot(slot).getItem()).getCapacity();
//            }
        }
        return out;
    }

    public void onContentsChanged(int slot) {
        initCapacity();
        if(owner != null && !owner.level.isClientSide) {
            CybPackets.sendToClient(new S2CSyncCyberwarePacket(owner, this, true), (ServerPlayer) owner);
        }
    }

    public void copyFrom(CyberwareInventory other, boolean shouldUpdate) {
        copyFrom(other, null, shouldUpdate);
    }

    public void copyFrom(CyberwareInventory other, Player player, boolean shouldUpdate) {
        for(int i = 0; i < this.getSlots(); i++) {
            ItemStack oldStack = this.getStackInSlot(i);
            ItemStack newStack = other.getStackInSlot(i);
            if(player != null && shouldUpdate && !oldStack.equals(newStack, false)) {
                if(oldStack.getItem() instanceof CyberwareItem) {
                    ((CyberwareItem) oldStack.getItem()).onUnequip(oldStack, player.level, player);
                }
                if(newStack.getItem() instanceof CyberwareItem) {
                    ((CyberwareItem) newStack.getItem()).onEquip(newStack, player.level, player);
                }
            }
            this.setStackInSlot(i, newStack.copy());
        }
        initCapacity();
        this.maxCapacity = other.getMaxCapacity();
    }

    public void clear() {
        for(int i = 0; i < getSlots(); i++) {
            setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public List<CyberwareSection> getSections() {
        List<CyberwareSection> output = new ArrayList<>();
        for(int i = 0; i < itemHandler.length; i++) {
            output.add((CyberwareSection) itemHandler[i]);
        }
        return output;
    }


    public CyberwareSection getSectionFromSlot(int slot) {
        int index = getIndexForSlot(slot);
        return ((CyberwareSection) getHandlerFromIndex(index));
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CyberwareInventory other)) return false;
        if(other.getSlots() != this.getSlots()) return false;
        for(int i = 0; i < this.getSlots(); i++) {
            if(!this.getStackInSlot(i).equals(other.getStackInSlot(i), false)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for(IItemHandlerModifiable handler : itemHandler) {
            CyberwareSection section = (CyberwareSection) handler;
            tag.put(section.getId().toString(), section.serializeNBT());
        }
        tag.putInt("max_capacity", maxCapacity);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for(IItemHandlerModifiable handler : itemHandler) {
            CyberwareSection section = (CyberwareSection) handler;
            section.deserializeNBT(tag.getCompound(section.getId().toString()));
        }
        maxCapacity = tag.getInt("max_capacity");
        initCapacity();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        //small optimization, only loop if has req/incompatibilities
        if(stack.getItem() instanceof CyberwareItem item && (item.getRequirements().size() > 0 || item.getIncompatibilities().size() > 0)) {
            Map<Ingredient, Boolean> metRequirements = new HashMap<>();
            for(Ingredient req : item.getRequirements()) {
                metRequirements.put(req, false);
            }
            for(int i = 0; i < this.getSlots(); i++) {
                if(this.getStackInSlot(i).is(item)) return false;

                //usually short lists so not that bad tbh
                for(Ingredient incompat : item.getIncompatibilities()) {
                    if(incompat.test(this.getStackInSlot(i))) return false;
                }
                for(Ingredient req : item.getRequirements()) {
                    if(req.test(this.getStackInSlot(i))) {
                        metRequirements.put(req, true);
                    }
                }
            }
            for(Boolean val : metRequirements.values()) {
                if(!val) return false;
            }
        }
        return super.isItemValid(slot, stack);
    }

    public int getLongestSectionSize() {
        int output = 0;
        for(int i = 0; i < itemHandler.length; i++) {
            if(output < itemHandler[i].getSlots()) {
                output = itemHandler[i].getSlots();
            }
        }
        return output;
    }

    public void initCapacity() {
        capacity = 0;
        for(int i = 0; i < getSlots(); i++) {
            Item item = getStackInSlot(i).getItem();
            if(item instanceof CyberwareItem) {
                capacity += ((CyberwareItem) item).getCapacity();
            }
        }
    }

    public int getStoredCapacity() {
        return capacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void syncToClient(ServerPlayer client) {
        CybPackets.sendToClient(new S2CSyncCyberwarePacket(owner, this, false), client);
    }
}
