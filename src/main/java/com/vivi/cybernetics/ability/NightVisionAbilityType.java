package com.vivi.cybernetics.ability;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class NightVisionAbilityType extends AbilityType {

    public NightVisionAbilityType() {

    }

    @Override
    public void tick(Ability ability, Player player) {
        super.tick(ability, player);
        if(player.level.isClientSide) return;
        if(ability.isEnabled()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 319, 0, false, false, true));
        }
    }

    @Override
    public void onDisable(Ability ability, Player player) {
        super.onDisable(ability, player);
        if(player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).getAmplifier() == 0) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
