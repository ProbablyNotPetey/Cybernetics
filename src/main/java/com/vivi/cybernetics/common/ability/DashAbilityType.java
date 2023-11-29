package com.vivi.cybernetics.common.ability;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@HiddenAbility
public class DashAbilityType extends AbilityType {

    public DashAbilityType() {
        super(100);
    }
    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        ability.disable(player);


    }
}
