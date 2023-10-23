package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.item.ReinforcedTendonsItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDoubleJumpPacket extends Packet {

    public C2SDoubleJumpPacket() {

    }

    public C2SDoubleJumpPacket(FriendlyByteBuf buf) {

    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            ReinforcedTendonsItem.doubleJump(player);
        });
        return true;
    }
}
