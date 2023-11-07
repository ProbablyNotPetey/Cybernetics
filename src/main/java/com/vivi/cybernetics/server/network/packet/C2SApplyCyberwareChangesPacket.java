package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.menu.CyberwareMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SApplyCyberwareChangesPacket extends Packet {

    public C2SApplyCyberwareChangesPacket() {

    }

    public C2SApplyCyberwareChangesPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player.containerMenu instanceof CyberwareMenu menu) {
                menu.applyChanges(player);
            }
        });
        return true;
    }
}
