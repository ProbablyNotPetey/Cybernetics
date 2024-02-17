package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.particle.type.BlastWaveParticleType;
import com.vivi.cybernetics.client.particle.type.FallingParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CybParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Cybernetics.MOD_ID);

    public static final RegistryObject<BlastWaveParticleType> BLAST_WAVE =
            PARTICLE_TYPES.register("blast_wave", () -> new BlastWaveParticleType(true));
    public static final RegistryObject<FallingParticleType> FALLING_PARTICLE =
            PARTICLE_TYPES.register("falling_particle", () -> new FallingParticleType(false));



    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
