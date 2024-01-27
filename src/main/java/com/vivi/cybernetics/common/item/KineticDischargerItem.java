package com.vivi.cybernetics.common.item;

import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.registry.CybParticles;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.lodestone.CustomPositionedScreenshakePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.easing.Easing;

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

        double y = player.getDeltaMovement().y;
        if(y > 0) y = 0.0;
        player.setDeltaMovement(0, (y- 0.6) * 2 , 0);
//        if (player.isSprinting()) {
//            float f = player.getYRot() * ((float)Math.PI / 180F);
//            player.setDeltaMovement(player.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0D, (double)(Mth.cos(f) * 0.2F)));
//        }

        player.hasImpulse = true;
    }

    public static void shockwave(Player player, Level level) {
//        BlockPos pos = new BlockPos((int)player.position().x, (int)(player.getBoundingBox().minY - 0.5000001D), (int)player.position().z);
        BlockPos pos = player.blockPosition().subtract(new Vec3i(0, 1, 0));

        BlockPos.betweenClosed(pos.offset(3, 0, 3), pos.offset(-3, 0, -3)).forEach(blockPos -> {

            BlockPos difference = blockPos.subtract(pos);
            if(Math.abs(difference.getX()) == 3 && Math.abs(difference.getZ()) == 3) return;

            BlockState block = level.getBlockState(blockPos);
            level.levelEvent(2001, blockPos, Block.getId(block));
        });

        AABB box = new AABB(player.blockPosition()).inflate(3);
        level.getEntities(player, box).forEach(entity -> {
            if (!(entity instanceof LivingEntity) || !entity.isAlive()) {
                return;
            }
            entity.hurt(level.damageSources().playerAttack(player), 9.0f);
            entity.push(0, 0.8, 0);
        });


        //todo: make this run 1 tick later
        if(level instanceof ServerLevel server) {
            server.sendParticles(CybParticles.BLAST_WAVE.get(), player.position().x, player.position().y + 0.01, player.position().z, 1, 0, 0, 0, 0);

            CybPackets.getInstance().send(
                    PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(player.position().x, player.position().y, player.position().z, 10.0, player.level().dimension())),
                    new CustomPositionedScreenshakePacket(3, true, player.position(), 5.0f, 10.0f).setIntensity(0.7f).setEasing(Easing.EXPO_OUT, Easing.SINE_IN_OUT)
            );
        }

    }
}
