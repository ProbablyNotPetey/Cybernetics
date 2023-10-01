package com.vivi.cybernetics.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;

public class MobEffectCyberware extends CyberwareItem {

    private final Triple<MobEffect, Integer, Integer>[] effects;

    //effect, duration, amplifier
    @SafeVarargs
    public MobEffectCyberware(Properties pProperties, Triple<MobEffect, Integer, Integer>... effects) {
        super(pProperties);
        this.effects = effects;
    }

    @Override
    public void cyberwareTick(ItemStack stack, Level level, Player player) {
        super.cyberwareTick(stack, level, player);
        for(int i = 0; i < effects.length; i++) {
            Triple<MobEffect, Integer, Integer> effect = effects[i];
            player.addEffect(new MobEffectInstance(effect.getLeft(), effect.getMiddle(), effect.getRight(), false, false, true));
        }
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        //if player has higher level effect do not remove
        for(int i = 0; i < effects.length; i++) {
            Triple<MobEffect, Integer, Integer> effect = effects[i];
            if(player.hasEffect(effect.getLeft()) && player.getEffect(effect.getLeft()).getAmplifier() == effect.getRight()) {
                player.removeEffect(effect.getLeft());
            }
        }

    }
}
