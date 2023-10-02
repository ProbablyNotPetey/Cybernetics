package com.vivi.cybernetics.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

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
}
