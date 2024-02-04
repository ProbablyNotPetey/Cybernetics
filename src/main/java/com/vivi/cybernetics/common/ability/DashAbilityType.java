package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.item.DashCyberwareItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DashAbilityType extends AbilityType {

    private final boolean canDashMidair;
    public DashAbilityType(boolean canDashMidair) {
        super(100, 0);
        this.canDashMidair = canDashMidair;
    }
    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        DashCyberwareItem.dash(player);
    }

    public boolean canDash(Ability ability, Level level, Player player) {
        if(player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) return false;
        return !(ability.getCooldown() > -1) && (canDashMidair || (player.onGround() || player.onClimbable()));
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
