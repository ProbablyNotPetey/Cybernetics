package com.vivi.cybernetics.common.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.common.item.DashCyberwareItem;
import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.server.data.CyberwareProperties;
import com.vivi.cybernetics.server.data.CyberwarePropertiesReloadListener;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.registry.CybCyberware;
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

    public static void hideRequirements(Item item) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).hideRequirements();
    }

    public static void hideIncompatibilities(Item item) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).hideIncompatibilities();
    }

    public static void hideDescription(Item item) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).hideDescription();
    }

    public static void setCapacity(Item item, int capacity) {
        if(!(item instanceof CyberwareItem)) {
            Cybernetics.LOGGER.warn("Item " + item + " does not extend CyberwareItem, skipping...");
            return;
        }
        ((CyberwareItem) item).setCapacity(capacity);
    }



    public static void setupCyberwareProperties() {
        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            ResourceLocation id = entry.getKey().location();
            Item item = entry.getValue();
            if(item instanceof CyberwareItem) {
                CyberwareProperties properties = CyberwarePropertiesReloadListener.INSTANCE.getProperties().get(id);
                if(properties != null) {
                    List<Ingredient> requirements = properties.getRequirements();
                    if(requirements.size() > 0) addRequirements(item, requirements.toArray(new Ingredient[0]));

                    List<Ingredient> incompatibilities = properties.getIncompatibilities();
                    if(incompatibilities.size() > 0) addIncompatibilities(item, incompatibilities.toArray(new Ingredient[0]));
                    if(!properties.showRequirements()) hideRequirements(item);
                    if(!properties.showIncompatibilities()) hideIncompatibilities(item);
                    if(!properties.showDescription()) hideDescription(item);
                    setCapacity(item, properties.getCapacity());
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
        if(player == null) return LazyOptional.empty();
        return player.getCapability(Cybernetics.CYBERWARE);
    }

    /**
     * -1 if cannot dash, 0 if can dash on ground, 1 if can dash in midair
     */
    //todo: redo, switch to use abilities
    public static int canDash(Player player) {
        if(player.getCooldowns().isOnCooldown(CybItems.MK1_DASH.get())) return -1;
        CyberwareInventory cyberware = getCyberware(player).orElse(null);
        if(cyberware == null) return -1;
        for(int i = 0; i < cyberware.getSlots(); i++) {
            if(cyberware.getStackInSlot(i).getItem() instanceof DashCyberwareItem item) {
                if(item.canDashMidair()) return 1;
                return 0;
            }
        }
        return -1;
    }
}
