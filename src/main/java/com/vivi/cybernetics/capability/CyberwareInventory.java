package com.vivi.cybernetics.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.LinkedHashMap;
import java.util.Map;

//todo: implement IItemHandler, do something similar to CombinedInvWrapper
public class CyberwareInventory {

    private final LinkedHashMap<String, ItemStackHandler> parts = new LinkedHashMap<>();

    private int size;

    public CyberwareInventory() {
        //2 head, 2 eye, 3 upper organs, 3 lower organs, 3 skeleton, 3 skin, 1 hands, 1 arms, 1 legs, 1 feet
        parts.put("head", new CyberwareItemHandler(2)); //0, 0 1 -> 0 1 (+0)
        parts.put("eyes", new CyberwareItemHandler(2)); //1, 0 1 -> 2 3 (+2)
        parts.put("upper_organs", new CyberwareItemHandler(3)); //2, 0 1 2 -> 4 5 6 (+4)
        parts.put("lower_organs", new CyberwareItemHandler(3)); //3, 0 1 2 -> 7 8 9 (+7)
        parts.put("skeleton", new CyberwareItemHandler(3)); //4, 0 1 2 -> 10 11 12 (+10)
        parts.put("skin", new CyberwareItemHandler(3));
        parts.put("hands", new CyberwareItemHandler(1));
        parts.put("arms", new CyberwareItemHandler(1));
        parts.put("legs", new CyberwareItemHandler(1));
        parts.put("feet", new CyberwareItemHandler(1));

        for (ItemStackHandler value : parts.values()) {
            size += value.getSlots();
        }
    }
    public CyberwareInventory(CyberwareInventory other) {
        this.copyFrom(other);
    }


    public Map<String, ItemStackHandler> getParts() {
        return parts;
    }
    public int getSize() {
        return size;
    }

    public ItemStack removeItem(int index) {
//        int offset = 0;
        int i = 0;
//        int oldValue = 0;
        for(Map.Entry<String, ItemStackHandler> entry : parts.entrySet()) {
//            if(i > 0) {
//                offset += oldValue;
//            }
//            oldValue = entry.getValue().getSlots();
            for(int j = 0; j < entry.getValue().getSlots(); j++) {

                if(i == index) {
                    return entry.getValue().extractItem(j, entry.getValue().getStackInSlot(j).getCount(), false);
                }
                i++;
            }
        }

        return ItemStack.EMPTY;
    }

    public void saveNBT(CompoundTag tag) {
//        final NonNullList<ItemStack> itemList = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
//        for (int i = 0; i < itemList.size(); i++) {
//            itemList.set(i, getItem(i));
//        }
//        ContainerHelper.saveAllItems(tag, itemList, false);
        parts.forEach((k, v) -> {
            tag.put(k, v.serializeNBT());
        });

    }

    public void loadNBT(CompoundTag tag) {
//        final NonNullList<ItemStack> itemList = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
//        ContainerHelper.loadAllItems(tag, itemList);
//        for (int i = 0; i < itemList.size(); i++) {
//            setItem(i, itemList.get(i));
//        }
        parts.forEach((k, v) -> {
            v.deserializeNBT(tag.getCompound(k));
        });
    }

    public void copyFrom(CyberwareInventory other) {
        parts.clear();
        other.getParts().forEach(parts::put);
    }

//    public NonNullList<ItemStack> getItems() {
//        final NonNullList<ItemStack> output = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
//        for(int i = 0; i < output.size(); i++) {
//            output.set(i, getItem(i));
//        }
//        return output;
//    }



    static class CyberwareItemHandler extends ItemStackHandler {

        public CyberwareItemHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

    }
}
