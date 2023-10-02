package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.capability.PlayerEnergyStorage;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModCyberware {

    public static Capability<CyberwareInventory> CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<PlayerEnergyStorage> PLAYER_ENERGY = CapabilityManager.get(new CapabilityToken<>() { });

    public static final DeferredRegister<CyberwareSectionType> CYBERWARE_SECTION_TYPES = DeferredRegister.create(new ResourceLocation(Cybernetics.MOD_ID, "cybeware_section_types"), Cybernetics.MOD_ID);
    public static final Supplier<IForgeRegistry<CyberwareSectionType>> CYBERWARE_SECTION_TYPE_REGISTRY = CYBERWARE_SECTION_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<CyberwareSectionType>
            HEAD = CYBERWARE_SECTION_TYPES.register("head", () -> new CyberwareSectionType(ModTags.HEAD_SECTION, 5)),
            EYES = CYBERWARE_SECTION_TYPES.register("eyes", () -> new CyberwareSectionType(ModTags.EYES_SECTION, 4)),
            UPPER_ORGANS = CYBERWARE_SECTION_TYPES.register("upper_organs", () -> new CyberwareSectionType(ModTags.UPPER_ORGANS_SECTION, 7)),
            LOWER_ORGANS = CYBERWARE_SECTION_TYPES.register("lower_organs", () -> new CyberwareSectionType(ModTags.LOWER_ORGANS_SECTION, 6)),
            SKELETON = CYBERWARE_SECTION_TYPES.register("skeleton", () -> new CyberwareSectionType(ModTags.SKELETON_SECTION, 4)),
            SKIN = CYBERWARE_SECTION_TYPES.register("skin", () -> new CyberwareSectionType(ModTags.SKIN_SECTION, 3)),
            HANDS = CYBERWARE_SECTION_TYPES.register("hands", () -> new CyberwareSectionType(ModTags.HANDS_SECTION, 3)),
            ARMS = CYBERWARE_SECTION_TYPES.register("arms", () -> new CyberwareSectionType(ModTags.ARMS_SECTION, 4)),
            LEGS = CYBERWARE_SECTION_TYPES.register("legs", () -> new CyberwareSectionType(ModTags.LEGS_SECTION, 3)),
            FEET = CYBERWARE_SECTION_TYPES.register("feet", () -> new CyberwareSectionType(ModTags.FEET_SECTION, 4))
        ;

    public static void register(IEventBus eventBus) {
        CYBERWARE_SECTION_TYPES.register(eventBus);
    }

}
