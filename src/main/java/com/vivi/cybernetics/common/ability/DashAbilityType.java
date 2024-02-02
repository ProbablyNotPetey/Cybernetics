package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.item.DashCyberwareItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@HiddenAbility
public class DashAbilityType extends AbilityType {

    private final boolean canDashMidair;
    public DashAbilityType(boolean canDashMidair) {
        super(100);
        this.canDashMidair = canDashMidair;
    }
    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        DashCyberwareItem.dash(player);
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(ability.isEnabled()) {
            ability.disable(player);
        }
    }

    public boolean canDash(Ability ability, Level level, Player player) {
        if(player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) return false;
        return !(ability.getCooldown() > -1) && (canDashMidair || (player.onGround() || player.onClimbable()));
    }
}
