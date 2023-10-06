package com.vivi.cybernetics.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class Packet {
    public Packet() {

    }
    public Packet(FriendlyByteBuf buf) {

    }
    public abstract void toBytes(FriendlyByteBuf buf);
    public abstract boolean handle(Supplier<NetworkEvent.Context> sup);
}
