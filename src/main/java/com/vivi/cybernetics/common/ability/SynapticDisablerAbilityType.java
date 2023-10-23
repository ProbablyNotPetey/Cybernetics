package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.common.registry.CybMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SynapticDisablerAbilityType extends AbilityType {

    public SynapticDisablerAbilityType() {
        super(100, CybItems.SYNAPTIC_DISABLER.get());
    }

    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        ability.disable(player);
        ability.setCooldown(maxCooldown);
        if(level.isClientSide) return;
        //do stuff;
        //todo: implement
        level.getEntities(player, player.getBoundingBox().inflate(4, 0, 4)).forEach(entity -> {
            if(!(entity instanceof LivingEntity)) return;
            ((LivingEntity) entity).addEffect(new MobEffectInstance(CybMobEffects.PARALYZED.get(), 30, 0));
        });
    }
}
