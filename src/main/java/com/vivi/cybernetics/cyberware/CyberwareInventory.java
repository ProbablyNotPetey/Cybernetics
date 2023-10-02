package com.vivi.cybernetics.cyberware;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CyberwareInventory extends CombinedInvWrapper implements INBTSerializable<CompoundTag> {

    public CyberwareInventory(IItemHandlerModifiable... itemHandler) {
        super(itemHandler);
    }

    private static List<IItemHandlerModifiable> sections = new ArrayList<>();;

    public static CyberwareInventory create() {
                List<IItemHandlerModifiable> sections = new ArrayList<>();
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "head"), 5, ModTags.HEAD));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "eyes"), 4, ModTags.EYES));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "upper_organs"), 7, ModTags.UPPER_ORGANS));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "lower_organs"), 6, ModTags.LOWER_ORGANS));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skeleton"), 4, ModTags.SKELETON));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skin"), 3, ModTags.SKIN));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "hands"), 3, ModTags.HANDS));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "arms"), 4, ModTags.ARMS));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "legs"), 3, ModTags.LEGS));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "feet"), 4, ModTags.FEET));
        return new CyberwareInventory(sections.toArray(new IItemHandlerModifiable[0]));
    }
    public static void registerCyberwareSection(CyberwareSection section) {
        sections.add(section);
    }

    public void copyFrom(CyberwareInventory other) {
        copyFrom(other, null, false);
    }

    public void copyFrom(CyberwareInventory other, boolean isClone) {
        copyFrom(other, null, isClone);
    }

    public void copyFrom(CyberwareInventory other, Player player, boolean isClone) {
        for(int i = 0; i < this.getSlots(); i++) {
//            ItemStack oldStack = this.getStackInSlot(i);
//            ItemStack newStack = other.getStackInSlot(i);
//            if(player != null && !isClone && !oldStack.equals(newStack, false)) {
//                if(oldStack.getItem() instanceof CyberwareItem) ((CyberwareItem) oldStack.getItem()).onUnequip(oldStack, player.level, player);
//                if(newStack.getItem() instanceof CyberwareItem) ((CyberwareItem) newStack.getItem()).onEquip(newStack, player.level, player);
//            }
            this.setStackInSlot(i, other.getStackInSlot(i).copy());
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
            tag.put(section.id.toString(), section.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for(IItemHandlerModifiable handler : itemHandler) {
            CyberwareSection section = (CyberwareSection) handler;
            section.deserializeNBT(tag.getCompound(section.id.toString()));
        }
    }

}
