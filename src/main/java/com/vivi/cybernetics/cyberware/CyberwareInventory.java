package com.vivi.cybernetics.cyberware;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.network.packet.S2CSyncCyberwarePacket;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CyberwareInventory extends CombinedInvWrapper implements INBTSerializable<CompoundTag> {

    private final Player owner;
    public CyberwareInventory(Player owner, IItemHandlerModifiable... itemHandler) {
        super(itemHandler);
        this.owner = owner;
    }



    public static CyberwareInventory create(Player owner) {

        List<CyberwareSection> sections = new ArrayList<>();
        ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getEntries().forEach(type -> {
            sections.add(new CyberwareSection(type.getValue(), type.getKey().location()));
        });
        sections.sort((section1, section2) -> {
            int idx1 = CyberwareSection.SECTION_SORT.indexOf(section1.getId());
            int idx2 = CyberwareSection.SECTION_SORT.indexOf(section2.getId());
            int i = 0;
            if(idx1 == -1) {
                idx1 = CyberwareSection.SECTION_SORT.size() + i++;
            }
            if(idx2 == -1) {
                idx2 = CyberwareSection.SECTION_SORT.size() + i++;
            }
            return idx1 - idx2;
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
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack out = super.extractItem(slot, amount, simulate);
        if(!simulate) {
            onContentsChanged(slot);
        }
        return out;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack out =  super.insertItem(slot, stack, simulate);
        if(!simulate) {
            onContentsChanged(slot);
        }
        return out;
    }

    protected void onContentsChanged(int slot) {
        if(owner != null && !owner.level.isClientSide) {
            CybPackets.sendToClient(new S2CSyncCyberwarePacket(owner, this), (ServerPlayer) owner);
        }
    }

    public void copyFrom(CyberwareInventory other, boolean shouldUpdate) {
        copyFrom(other, null, shouldUpdate);
    }

    public void copyFrom(CyberwareInventory other, Player player, boolean shouldUpdate) {
        for(int i = 0; i < this.getSlots(); i++) {
            ItemStack oldStack = this.getStackInSlot(i);
            ItemStack newStack = other.getStackInSlot(i);
            if(player != null && !shouldUpdate && !oldStack.equals(newStack, false)) {
                if(oldStack.getItem() instanceof CyberwareItem) ((CyberwareItem) oldStack.getItem()).onUnequip(oldStack, player.level, player);
                if(newStack.getItem() instanceof CyberwareItem) ((CyberwareItem) newStack.getItem()).onEquip(newStack, player.level, player);
            }
            this.setStackInSlot(i, newStack.copy());
        }
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
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for(IItemHandlerModifiable handler : itemHandler) {
            CyberwareSection section = (CyberwareSection) handler;
            section.deserializeNBT(tag.getCompound(section.getId().toString()));
        }
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
}
