package com.vivi.cybernetics.capability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CyberwareInventory extends CombinedInvWrapper implements INBTSerializable<CompoundTag> {

    public CyberwareInventory(IItemHandlerModifiable... itemHandler) {
        super(itemHandler);
    }

    public static CyberwareInventory create() {
        List<IItemHandlerModifiable> sections = new ArrayList<>();
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "head"), 2));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "eyes"), 2));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "upper_organs"), 3));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "lower_organs"), 3));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skeleton"), 3));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skin"), 3));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "hands"), 1));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "arms"), 1));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "legs"), 1));
        sections.add(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "feet"), 1));
        return new CyberwareInventory(sections.toArray(new IItemHandlerModifiable[0]));
    }

    public void copyFrom(CyberwareInventory other) {
        for(int i = 0; i < other.getSlots(); i++) {
            this.setStackInSlot(i, other.getStackInSlot(i));
        }
    }


    public CyberwareSection getSectionFromSlot(int slot) {
        int index = getIndexForSlot(slot);
        return ((CyberwareSection) getHandlerFromIndex(index));
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
