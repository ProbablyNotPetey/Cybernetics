package com.vivi.cybernetics.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModCyberware;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class CyberwareHelper {

    public static void addIncompatibilities(Item item, Ingredient... incompatibilities) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).addIncompatibilities(incompatibilities);
    }

    public static void addRequirements(Item item, Ingredient... requirements) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).addRequirements(requirements);
    }

    public static List<CyberwareSectionType> getValidCyberwareSections(ItemStack stack) {
        List<CyberwareSectionType> out = new ArrayList<>();

        ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getValues().forEach(type -> {
            if(stack.is(type.getTag())) {
                out.add(type);
            }
        });

        return out;
    }

    public static boolean hasCyberwareItem(Player player, Item item) {
        CyberwareInventory cyberwareInventory = player.getCapability(ModCyberware.CYBERWARE).orElse(null);
        if(cyberwareInventory == null) return false;
        for(int i = 0; i < cyberwareInventory.getSlots(); i++) {
            if(cyberwareInventory.getStackInSlot(i).is(item)) {
                return true;
            }
        }
        return false;
    }
}
