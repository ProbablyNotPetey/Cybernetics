package com.vivi.cybernetics.item;

import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.registry.CybAbilities;
import com.vivi.cybernetics.util.AbilityHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NightVisionEyesItem extends CyberwareItem {

    public NightVisionEyesItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEquip(ItemStack stack, Level level, Player player) {
        super.onEquip(stack, level, player);
        AbilityHelper.addAbility(player, CybAbilities.NIGHT_VISION.get());
        AbilityHelper.enableAbility(player, CybAbilities.NIGHT_VISION.get());
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        AbilityHelper.removeAbility(player, CybAbilities.NIGHT_VISION.get());
    }

    @Override
    public void cyberwareTick(ItemStack stack, Level level, Player player) {
        super.cyberwareTick(stack, level, player);
    }
}
