package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NightVisionAbilityType extends AbilityType {

    public NightVisionAbilityType() {
        super(new ResourceLocation(Cybernetics.MOD_ID, "textures/item/night_vision_eyes.png"));
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(level.isClientSide) return;
        if(ability.isEnabled()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false, false));
        }
    }

    @Override
    public void onDisable(Ability ability, Level level, Player player) {
        super.onDisable(ability, level, player);
        if(level.isClientSide) return;
        if(player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).getAmplifier() == 0) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
