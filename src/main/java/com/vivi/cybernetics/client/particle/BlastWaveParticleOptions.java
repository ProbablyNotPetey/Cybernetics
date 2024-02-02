package com.vivi.cybernetics.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class BlastWaveParticleOptions implements ParticleOptions {

    public final ParticleType<BlastWaveParticleOptions> type;
    public final int argb;
    public final int duration;
    public final float radius;
    public static final Function<ParticleType<BlastWaveParticleOptions>, Codec<BlastWaveParticleOptions>> CODEC = type -> {
        return RecordCodecBuilder.create(builder -> {
            return builder.group(
                    Codec.INT.fieldOf("argb").forGetter(options -> options.argb),
                    Codec.INT.fieldOf("duration").forGetter(options -> options.duration),
                    Codec.FLOAT.fieldOf("radius").forGetter(options -> options.radius)
            ).apply(builder, (argb, duration, radius) -> {
                return new BlastWaveParticleOptions(type, argb, duration, radius);
            });
        });
    };

    public static final ParticleOptions.Deserializer<BlastWaveParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public BlastWaveParticleOptions fromCommand(ParticleType<BlastWaveParticleOptions> pParticleType, StringReader pReader) throws CommandSyntaxException {
            return new BlastWaveParticleOptions(pParticleType, pReader);
        }

        @Override
        public BlastWaveParticleOptions fromNetwork(ParticleType<BlastWaveParticleOptions> pParticleType, FriendlyByteBuf pBuffer) {
            return new BlastWaveParticleOptions(pParticleType, pBuffer.readVarInt(), pBuffer.readVarInt(), pBuffer.readFloat());
        }
    };

    public BlastWaveParticleOptions(ParticleType<BlastWaveParticleOptions> type, int argb, int duration, float radius) {
        this.type = type;
        this.argb = argb;
        this.duration = duration;
        this.radius = radius;
    }
    public BlastWaveParticleOptions(ParticleType<BlastWaveParticleOptions> type) {
        this(type, 0xFFFFFFFF, 20, 8.0f);
    }
    public BlastWaveParticleOptions(ParticleType<BlastWaveParticleOptions> type, int argb) {
        this(type, argb, 20, 8.0f);
    }
    public BlastWaveParticleOptions(ParticleType<BlastWaveParticleOptions> type, int argb, int duration) {
        this(type, argb, duration, 8.0f);
    }

    protected BlastWaveParticleOptions(ParticleType<BlastWaveParticleOptions> type, StringReader reader) throws CommandSyntaxException {
        this.type = type;
        reader.expect(' ');
        this.argb = reader.readInt();
        reader.expect(' ');
        this.duration = reader.readInt();
        reader.expect(' ');
        this.radius = (float)reader.readDouble();
    }

    @Override
    public ParticleType<BlastWaveParticleOptions> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeVarInt(argb);
        buf.writeVarInt(duration);
        buf.writeFloat(radius);
    }

    @Override
    public String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(type) + ", " + argb + ", " + duration + ", " + radius;
    }
}
