package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.ability.AbilityType;
import com.vivi.cybernetics.common.ability.NightVisionAbilityType;
import com.vivi.cybernetics.common.ability.ScanAbilityType;
import com.vivi.cybernetics.common.ability.SynapticDisablerAbilityType;
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
            SCAN = ABILITY_TYPES.register("scan", ScanAbilityType::new)
    ;


    public static void register(IEventBus eventBus) {
        ABILITY_TYPES.register(eventBus);
    }

}
