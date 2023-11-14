package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.ability.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CybAbilities {

    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(new ResourceLocation(Cybernetics.MOD_ID, "ability_types"), Cybernetics.MOD_ID);
    public static final Supplier<IForgeRegistry<AbilityType>> ABILITY_TYPE_REGISTRY = ABILITY_TYPES.makeRegistry(RegistryBuilder::new);


    public static final RegistryObject<AbilityType>
            NIGHT_VISION = ABILITY_TYPES.register("night_vision", NightVisionAbilityType::new),
            SYNAPTIC_DISABLER = ABILITY_TYPES.register("synaptic_disabler", SynapticDisablerAbilityType::new),
            SCAN = ABILITY_TYPES.register("scan", ScanAbilityType::new),
            HUD = ABILITY_TYPES.register("hud", HUDAbilityType::new),
            MK1_BERSERK = ABILITY_TYPES.register("mk1_berserk", () -> new BerserkAbilityType(CybItems.MK1_BERSERK.get(), 200, 1200)),
            MK2_BERSERK = ABILITY_TYPES.register("mk2_berserk", () -> new BerserkAbilityType(CybItems.MK2_BERSERK.get(), 240, 1000, 3.0, 0, 0.3)),
            MK3_BERSERK = ABILITY_TYPES.register("mk3_berserk", () -> new BerserkAbilityType(CybItems.MK3_BERSERK.get(), 300, 900, 5.5, 1, 0.5))
    ;


    public static void register(IEventBus eventBus) {
        ABILITY_TYPES.register(eventBus);
    }

}
