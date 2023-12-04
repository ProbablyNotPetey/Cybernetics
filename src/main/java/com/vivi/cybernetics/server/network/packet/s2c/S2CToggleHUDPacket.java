package com.vivi.cybernetics.server.network.packet.s2c;

import com.vivi.cybernetics.server.network.ClientPacketHandler;
import com.vivi.cybernetics.server.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CToggleHUDPacket extends Packet {

    private final boolean on;
    public S2CToggleHUDPacket(boolean on) {
        this.on = on;
    }

    public S2CToggleHUDPacket(FriendlyByteBuf buf) {
        this.on = buf.readBoolean();
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(on);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleHUDPacket(ctx, this));
        });
        return true;
    }

    public boolean isOn() {
        return on;
    }
}
