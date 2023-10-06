package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.ReinforcedTendonsItem;
import com.vivi.cybernetics.network.PacketHandler;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModItems;
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
            Cybernetics.LOGGER.info("Received Double Jump packet");
            ServerPlayer player = ctx.getSender();
            if(CyberwareHelper.hasCyberwareItem(player, ModItems.REINFORCED_TENDONS.get())) {
                PacketHandler.sendToClient(new S2CDoubleJumpPacket(), player);
                ReinforcedTendonsItem.doubleJump(player);
            }
        });
        return true;
    }
}
