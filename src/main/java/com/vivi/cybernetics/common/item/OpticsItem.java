package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OpticsItem extends CyberwareItem {

    private final boolean canScan;
    public OpticsItem(Properties pProperties, boolean canScan) {
        super(pProperties);
        this.canScan = canScan;
    }

    @Override
    public void onEquip(ItemStack stack, Level level, Player player) {
        super.onEquip(stack, level, player);
        if(canScan) AbilityHelper.addAbility(player, CybAbilities.SCAN.get());
        AbilityHelper.addAbility(player, CybAbilities.HUD.get());
        AbilityHelper.enableAbility(player, CybAbilities.HUD.get());
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        if(canScan) AbilityHelper.removeAbility(player, CybAbilities.SCAN.get());
        AbilityHelper.removeAbility(player, CybAbilities.HUD.get());
    }
}
