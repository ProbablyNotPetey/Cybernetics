package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KineticDischargerItem extends CyberwareItem {
    public KineticDischargerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(ItemStack stack, Level level, Player player) {
        super.onEquip(stack, level, player);
        AbilityHelper.addAbility(player, CybAbilities.KINETIC_DISCHARGER.get());
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);
        AbilityHelper.removeAbility(player, CybAbilities.KINETIC_DISCHARGER.get());
    }
    public static void spike(Player player) {
//        Cybernetics.LOGGER.info("Spiking");
        player.fallDistance = 0;

        Vec3 movement = player.getDeltaMovement();
        if(movement.y > 0) movement = new Vec3(movement.x, 0.0, movement.z);
        player.setDeltaMovement(movement.x, ((movement.y)- 0.5) * 2 , movement.z);
//        if (player.isSprinting()) {
//            float f = player.getYRot() * ((float)Math.PI / 180F);
//            player.setDeltaMovement(player.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0D, (double)(Mth.cos(f) * 0.2F)));
//        }

        player.hasImpulse = true;
    }

    public static void shockwave(Player player, Level level) {
        BlockPos pos = new BlockPos(player.position().x, player.getBoundingBox().minY - 0.5000001D, player.position().z);

        BlockPos.betweenClosed(pos.offset(3, 0, 3), pos.offset(-3, 0, -3)).forEach(blockPos -> {

            BlockPos difference = blockPos.subtract(pos);
            if(Math.abs(difference.getX()) == 3 && Math.abs(difference.getZ()) == 3) return;

            BlockState block = level.getBlockState(blockPos);
            level.levelEvent(2001, blockPos, Block.getId(block));
        });

        AABB box = new AABB(pos.offset(0, 1, 0)).inflate(3);
        level.getEntities(player, box).forEach(entity -> {
            if (!(entity instanceof LivingEntity) || !entity.isAlive()) {
                return;
            }
            entity.hurt(DamageSource.playerAttack(player), 9.0f);
            entity.push(0, 0.8, 0);
        });

    }
}
