package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.util.CybMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CybMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Cybernetics.MOD_ID);

    public static final RegistryObject<MobEffect>
            PARALYZED = MOB_EFFECTS.register("paralyzed", () -> new CybMobEffect(MobEffectCategory.HARMFUL, 0xffff85).addAttributeModifier(Attributes.MOVEMENT_SPEED, "11f9b739-580b-4837-bc24-8bd8573a15c2", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL))
    ;

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
