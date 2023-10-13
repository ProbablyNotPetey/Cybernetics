package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.item.KineticDischargerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSpikeShockwavePacket extends Packet {
    public C2SSpikeShockwavePacket() {

    }

    public C2SSpikeShockwavePacket(FriendlyByteBuf buf) {

    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            KineticDischargerItem.shockwave(player, player.level);
        });
        return true;
    }
}
