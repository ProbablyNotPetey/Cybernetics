package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.common.util.Triple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MobEffectCyberwareItem extends CyberwareItem {

    private final Triple<MobEffect, Integer, Integer>[] effects;

    //effect, duration, amplifier
    @SafeVarargs
    public MobEffectCyberwareItem(Properties pProperties, Triple<MobEffect, Integer, Integer>... effects) {
        super(pProperties);
        this.effects = effects;
    }

    @Override
    public void cyberwareTick(ItemStack stack, Level level, Player player) {
        super.cyberwareTick(stack, level, player);
        if(level.isClientSide) return;
        for(int i = 0; i < effects.length; i++) {
            Triple<MobEffect, Integer, Integer> effect = effects[i];
            player.addEffect(new MobEffectInstance(effect.getFirst(), effect.getSecond(), effect.getThird(), false, false, true));
        }
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        if(level.isClientSide) return;
        //if player has higher level effect do not remove
        for(int i = 0; i < effects.length; i++) {
            Triple<MobEffect, Integer, Integer> effect = effects[i];
            if(player.hasEffect(effect.getFirst()) && player.getEffect(effect.getFirst()).getAmplifier() == effect.getThird()) {
                player.removeEffect(effect.getFirst());
            }
        }

    }
}
