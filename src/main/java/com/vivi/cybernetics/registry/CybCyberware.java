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

public class CybCyberware {

    public static Capability<CyberwareInventory> CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<PlayerEnergyStorage> PLAYER_ENERGY = CapabilityManager.get(new CapabilityToken<>() { });

    public static final DeferredRegister<CyberwareSectionType> CYBERWARE_SECTION_TYPES = DeferredRegister.create(new ResourceLocation(Cybernetics.MOD_ID, "cybeware_section_types"), Cybernetics.MOD_ID);
    public static final Supplier<IForgeRegistry<CyberwareSectionType>> CYBERWARE_SECTION_TYPE_REGISTRY = CYBERWARE_SECTION_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<CyberwareSectionType>
            HEAD = CYBERWARE_SECTION_TYPES.register("head", () -> new CyberwareSectionType(CybTags.HEAD_SECTION, 5, 49, 28, 20)),
            UPPER_ORGANS = CYBERWARE_SECTION_TYPES.register("upper_organs", () -> new CyberwareSectionType(CybTags.UPPER_ORGANS_SECTION, 7, 35, 46, -30)),
            SKELETON = CYBERWARE_SECTION_TYPES.register("skeleton", () -> new CyberwareSectionType(CybTags.SKELETON_SECTION, 4, 49, 64, -30)),
            LOWER_ORGANS = CYBERWARE_SECTION_TYPES.register("lower_organs", () -> new CyberwareSectionType(CybTags.LOWER_ORGANS_SECTION, 6, 35, 82, -70)),
            LEGS = CYBERWARE_SECTION_TYPES.register("legs", () -> new CyberwareSectionType(CybTags.LEGS_SECTION, 3, 49, 100, -130)),


            EYES = CYBERWARE_SECTION_TYPES.register("eyes", () -> new CyberwareSectionType(CybTags.EYES_SECTION, 4, 134, 28, 20)),
            ARMS = CYBERWARE_SECTION_TYPES.register("arms", () -> new CyberwareSectionType(CybTags.ARMS_SECTION, 4, 148, 46, 0)),
            SKIN = CYBERWARE_SECTION_TYPES.register("skin", () -> new CyberwareSectionType(CybTags.SKIN_SECTION, 3, 134, 64, -10)),
            HANDS = CYBERWARE_SECTION_TYPES.register("hands", () -> new CyberwareSectionType(CybTags.HANDS_SECTION, 3, 148, 82, -30)),
            FEET = CYBERWARE_SECTION_TYPES.register("feet", () -> new CyberwareSectionType(CybTags.FEET_SECTION, 4, 134, 100, -50))
        ;

    public static void register(IEventBus eventBus) {
        CYBERWARE_SECTION_TYPES.register(eventBus);
    }

}
