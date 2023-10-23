package com.vivi.cybernetics.server.network.packet;

/*
public class S2CDoubleJumpPacket extends Packet {

    public S2CDoubleJumpPacket() {

    }
    public S2CDoubleJumpPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleDoubleJumpPacket(ctx));
        });
        return true;
    }
}
 */
