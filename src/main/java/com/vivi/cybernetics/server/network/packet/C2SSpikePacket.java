package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.item.KineticDischargerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSpikePacket extends Packet {

    public C2SSpikePacket() {

    }

    public C2SSpikePacket(FriendlyByteBuf buf) {

    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            KineticDischargerItem.spike(player);
        });
        return true;
    }
}

