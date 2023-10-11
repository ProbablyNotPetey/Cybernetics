package com.vivi.cybernetics.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

//TODO: finish. Should remove all negative effects when eating food.
public class StomachFilterItem extends CyberwareItem {
    public StomachFilterItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void cyberwareTick(ItemStack stack, Level level, Player player) {
        super.cyberwareTick(stack, level, player);
        if(level.isClientSide) return;
        if(player.hasEffect(MobEffects.HUNGER)) {
            player.removeEffect(MobEffects.HUNGER);
        }
    }
}
