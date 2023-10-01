package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.cyberware.CyberwareSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static Capability<CyberwareInventory> CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<PlayerEnergyStorage> PLAYER_ENERGY = CapabilityManager.get(new CapabilityToken<>() { });

    //Cyberware sections should be registered before capabilities are gathered
    public static void registerCyberwareSections() {
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "head"), 5, ModTags.HEAD));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "eyes"), 4, ModTags.EYES));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "upper_organs"), 7, ModTags.UPPER_ORGANS));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "lower_organs"), 6, ModTags.LOWER_ORGANS));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skeleton"), 4, ModTags.SKELETON));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "skin"), 3, ModTags.SKIN));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "hands"), 3, ModTags.HANDS));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "arms"), 4, ModTags.ARMS));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "legs"), 3, ModTags.LEGS));
        CyberwareInventory.registerCyberwareSection(new CyberwareSection(new ResourceLocation(Cybernetics.MOD_ID, "feet"), 4, ModTags.FEET));
    }

}
