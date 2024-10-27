package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.item.DashCyberwareItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.joml.Vector3f;

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
//        Cybernetics.LOGGER.info("Trying to dash? " + level.isClientSide);
//        if(player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) return false;
//        Cybernetics.LOGGER.debug("Player movenet: " + player.getDeltaMovement());
        Vector3d movement = new Vector3d(player.getX(), player.getY(), player.getZ()).sub(new Vector3d(player.xOld, player.yOld, player.zOld));
        if(Math.abs(movement.x) <= 0 && Math.abs(movement.z) <= 0) return false;
//        Cybernetics.LOGGER.debug(new Vector3d(player.xOld, player.yOld, player.zOld) + " , " + new Vector3d(player.getX(), player.getY(), player.getZ()));
        Cybernetics.LOGGER.info("Can dash: " + ((canDashMidair || (player.onGround() || player.onClimbable()))));
        Cybernetics.LOGGER.debug("Cooldown: " + ability.getCooldown());
        return (ability.getCooldown() == -1) && (canDashMidair || (player.onGround() || player.onClimbable()));
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
