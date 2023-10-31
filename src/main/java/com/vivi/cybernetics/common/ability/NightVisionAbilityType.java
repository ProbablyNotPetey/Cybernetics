package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.registry.CybItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NightVisionAbilityType extends AbilityType {

    public NightVisionAbilityType() {
        super(CybItems.NIGHT_VISION_EYES.get());
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(level.isClientSide) return;
        if(ability.isEnabled()) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 319, 0, false, false, true));
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
