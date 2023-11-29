package com.vivi.cybernetics.common.ability;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@HiddenAbility
public class EmergencyDefibrillatorAbilityType extends AbilityType {

    public EmergencyDefibrillatorAbilityType() {
        super(2400);
    }

    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        ability.disable(player);
        if(level.isClientSide) return;
        player.setHealth(player.getMaxHealth() / 4.0f);
        //play some sound
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2));
    }
}
