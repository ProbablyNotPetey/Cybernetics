package com.vivi.cybernetics.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;

public class FallingParticleType extends ParticleType<WorldParticleOptions> {
    public FallingParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter, WorldParticleOptions.DESERIALIZER);
    }

    @Override
    public Codec<WorldParticleOptions> codec() {
        return WorldParticleOptions.worldCodec(this);
    }

    public static class Provider implements ParticleProvider<WorldParticleOptions> {

        @Nullable
        @Override
        public FallingParticle createParticle(WorldParticleOptions data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FallingParticle(pLevel, data, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
