package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.menu.CyberwareMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSwitchPagePacket extends Packet {

    private final int page;
    public C2SSwitchPagePacket(int page) {
        this.page = page;
    }
    public C2SSwitchPagePacket() {
        this.page = -1;
    }
    public C2SSwitchPagePacket(FriendlyByteBuf buf) {
        this.page = buf.readVarInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(page);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player.containerMenu instanceof CyberwareMenu menu) {
                menu.switchInventoryPage(page);
            }
        });
        return true;
    }
}
