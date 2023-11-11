package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.common.registry.CybItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DashCyberwareItem extends CyberwareItem {

    private final boolean canDashMidair;
    public DashCyberwareItem(Properties pProperties, boolean canDashMidair) {
        super(pProperties);
        this.canDashMidair = canDashMidair;
    }

    public boolean canDashMidair() {
        return canDashMidair;
    }

    public static void dash(Player player) {
        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(new Vec3(movement.x, 0.0, movement.z).normalize().scale(1.5));
        if(!player.level.isClientSide) {
            player.getCooldowns().addCooldown(CybItems.MK1_DASH.get(), 40);
        }
    }
}
