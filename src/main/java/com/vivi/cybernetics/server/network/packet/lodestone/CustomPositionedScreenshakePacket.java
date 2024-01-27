package com.vivi.cybernetics.server.network.packet.lodestone;

import com.vivi.cybernetics.client.util.CustomPositionedScreenshakeInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePacket;
import team.lodestar.lodestone.systems.easing.Easing;

import java.util.function.Supplier;

public class CustomPositionedScreenshakePacket extends PositionedScreenshakePacket {

    public final boolean alwaysApply;
    public CustomPositionedScreenshakePacket(int duration, boolean alwaysApply, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration, position, falloffDistance, maxDistance, falloffEasing);
        this.alwaysApply = alwaysApply;
    }

    public CustomPositionedScreenshakePacket(int duration, boolean alwaysApply, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, alwaysApply, position, falloffDistance, maxDistance, Easing.LINEAR);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ScreenshakeHandler.addScreenshake(new CustomPositionedScreenshakeInstance(duration, alwaysApply, position, falloffDistance, maxDistance, falloffEasing).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(duration);
        buf.writeBoolean(alwaysApply);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat(falloffDistance);
        buf.writeFloat(maxDistance);
        buf.writeUtf(falloffEasing.name);
        buf.writeFloat(intensity1);
        buf.writeFloat(intensity2);
        buf.writeFloat(intensity3);
        buf.writeUtf(intensityCurveStartEasing.name);
        buf.writeUtf(intensityCurveEndEasing.name);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, CustomPositionedScreenshakePacket.class, CustomPositionedScreenshakePacket::encode, CustomPositionedScreenshakePacket::decode, PositionedScreenshakePacket::handle);
    }

    public static CustomPositionedScreenshakePacket decode(FriendlyByteBuf buf) {

        return ((CustomPositionedScreenshakePacket)new CustomPositionedScreenshakePacket(
                buf.readInt(),
                buf.readBoolean(),
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(),
                buf.readFloat(),
                Easing.valueOf(buf.readUtf())
        ).setIntensity(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        ).setEasing(
                Easing.valueOf(buf.readUtf()),
                Easing.valueOf(buf.readUtf())
        ));
    }
}
