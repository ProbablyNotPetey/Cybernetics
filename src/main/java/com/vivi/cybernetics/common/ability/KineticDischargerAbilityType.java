package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.client.particle.FallingParticle;
import com.vivi.cybernetics.common.item.KineticDischargerItem;
import com.vivi.cybernetics.common.registry.CybParticles;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;


public class KineticDischargerAbilityType extends AbilityType {

    public KineticDischargerAbilityType() {
        super(100);
    }

    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        KineticDischargerItem.spike(player);
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(ability.isEnabled() && level.isClientSide && ability.getElapsedTime() % 10 == 0) {
            WorldParticleBuilder builder = WorldParticleBuilder.create(CybParticles.FALLING_PARTICLE)
                    .setRenderType(FallingParticle.FALLING_PARTICLE)
                    .setScaleData(GenericParticleData.create(1, 1, 1).build())
                    .setColorData(ColorParticleData.create(new Color(255, 232, 28), new Color(255, 232, 28)).build())
                    .setTransparencyData(GenericParticleData.create(0.4f, 0).setEasing(Easing.QUAD_IN).build())
                    .setShouldCull(false)
                    .setLifetime(20)
                    .setNoClip(true)
                    .setRandomOffset(5.0, 0.0, 5.0)
                    .setMotion(0.0, player.getDeltaMovement().y, 0.0);

            float max = 4.0f;
            float min = 1.5f;
            RandomSource random = level.random;

            for(int i = 0; i < 10; i++) {

                double yaw2 = random.nextFloat() * Math.PI * 2, pitch2 = random.nextFloat() * Math.PI - Math.PI / 2, xDist = random.nextFloat() * (max - min) + min, yDist = 0, zDist = random.nextFloat() * (max - min) + min;
                double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
                double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
                builder.spawn(level, player.position().x + xPos, player.position().y - 3.0, player.position().z + zPos);
            }
        }
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
