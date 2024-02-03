package com.vivi.cybernetics.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.Nullable;

public class BlastWaveParticleType extends ParticleType<BlastWaveParticleOptions> {
    public BlastWaveParticleType(boolean pOverrideLimiter) {
        super(pOverrideLimiter, BlastWaveParticleOptions.DESERIALIZER);
    }

    public BlastWaveParticleType() {
        this(true);
    }

    @Override
    public Codec<BlastWaveParticleOptions> codec() {
        return BlastWaveParticleOptions.CODEC.apply(this);
    }

    public static class Provider implements ParticleProvider<BlastWaveParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(BlastWaveParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BlastWaveParticle(options, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
