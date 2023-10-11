package com.vivi.cybernetics.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.data.CyberwareProperties;
import com.vivi.cybernetics.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.CybCyberware;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static void setupCyberwareProperties() {
        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            ResourceLocation id = entry.getKey().location();
            Item item = entry.getValue();
            if(item instanceof CyberwareItem) {
                CyberwareProperties properties = CyberwarePropertiesReloadListener.INSTANCE.getProperties().get(id);
                if(properties != null) {
                    List<Ingredient> requirements = properties.getRequirements();
                    Cybernetics.LOGGER.info("Item requirements size: " + ((CyberwareItem) item).getRequirements().size());
                    if(requirements.size() > 0) addRequirements(item, requirements.toArray(new Ingredient[0]));

                    List<Ingredient> incompatibilities = properties.getIncompatibilities();
                    Cybernetics.LOGGER.info("Item incompatibilities size: " + ((CyberwareItem) item).getIncompatibilities().size());
                    if(incompatibilities.size() > 0) addIncompatibilities(item, incompatibilities.toArray(new Ingredient[0]));
                }
            }
        });
    }

    public static List<CyberwareSectionType> getValidCyberwareSections(ItemStack stack) {
        List<CyberwareSectionType> out = new ArrayList<>();

        CybCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getValues().forEach(type -> {
            if(stack.is(type.getTag())) {
                out.add(type);
            }
        });

        return out;
    }

    /**
     * Checks if a player has a given cyberware item.
     */
    public static boolean hasCyberwareItem(Player player, Item item) {
        CyberwareInventory cyberwareInventory = getCyberware(player).orElse(null);
        if(cyberwareInventory == null) return false;
        for(int i = 0; i < cyberwareInventory.getSlots(); i++) {
            if(cyberwareInventory.getStackInSlot(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a player's cyberware as a lazy optional.
     */
    public static LazyOptional<CyberwareInventory> getCyberware(Player player) {
        return player.getCapability(Cybernetics.CYBERWARE);
    }
}
