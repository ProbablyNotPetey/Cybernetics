package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.registry.CybItems;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDoubleJumpInputPacket extends Packet {

    public C2SDoubleJumpInputPacket() {

    }

    public C2SDoubleJumpInputPacket(FriendlyByteBuf buf) {

    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(CyberwareHelper.hasCyberwareItem(player, CybItems.REINFORCED_TENDONS.get())) {
                CybPackets.sendToClient(new S2CDoubleJumpPacket(), player);
                ReinforcedTendonsItem.doubleJump(player);
            }
        });
        return true;
    }
}
