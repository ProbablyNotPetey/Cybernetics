package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.common.ability.AbilityType;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SimpleAbilityCyberwareItem extends CyberwareItem {

    private Supplier<AbilityType> type;
    public SimpleAbilityCyberwareItem(Properties pProperties, Supplier<AbilityType> type) {
        super(pProperties);
        this.type = type;
    }

    @Override
    public void onEquip(ItemStack stack, Level level, Player player) {
        super.onEquip(stack, level, player);
        AbilityHelper.addAbility(player, type.get());
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        AbilityHelper.removeAbility(player, type.get());
    }
}
