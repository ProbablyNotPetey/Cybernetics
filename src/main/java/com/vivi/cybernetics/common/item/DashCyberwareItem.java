package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.ability.AbilityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class DashCyberwareItem extends SimpleAbilityCyberwareItem {

    public DashCyberwareItem(Properties pProperties, Supplier<AbilityType> type) {
        super(pProperties, type);
    }

    public static void dash(Player player) {
        Vec3 movement = new Vec3(player.getDeltaMovement().x, 0.0, player.getDeltaMovement().z);
        Cybernetics.LOGGER.debug("Dashing!");
        Cybernetics.LOGGER.debug("Movement: " + new Vec3(movement.x, 0.0, movement.z));
        player.setDeltaMovement(movement.add(new Vec3(movement.x, 0.0, movement.z).normalize().scale(1.5)));
//        player.hurtMarked = true;
    }
}
