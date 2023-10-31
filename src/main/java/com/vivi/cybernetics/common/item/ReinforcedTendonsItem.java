package com.vivi.cybernetics.common.item;

import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ReinforcedTendonsItem extends CyberwareItem {
    public ReinforcedTendonsItem(Properties pProperties) {
        super(pProperties);
    }

    public static void doubleJump(Player player) {
        player.fallDistance = 0;

        double jumpMotion = (0.5) + player.getJumpBoostPower();
//        double jumpMotion = 3.5;
        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x, jumpMotion, movement.z);
        if (player.isSprinting()) {
            float f = player.getYRot() * ((float)Math.PI / 180F);
            player.setDeltaMovement(player.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0D, (double)(Mth.cos(f) * 0.2F)));
        }

        player.hasImpulse = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(player);

        player.awardStat(Stats.JUMP);
        if (player.isSprinting()) {
            player.causeFoodExhaustion(0.2F);
        } else {
            player.causeFoodExhaustion(0.05F);
        }

    }
}
