package com.vivi.cybernetics.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

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
}
