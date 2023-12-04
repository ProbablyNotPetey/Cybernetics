package com.vivi.cybernetics.server.network.packet.c2s;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.item.KineticDischargerItem;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.server.network.packet.Packet;
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
            if(!AbilityHelper.isEnabled(player, CybAbilities.KINETIC_DISCHARGER.get())) {
                AbilityHelper.enableAbility(player, CybAbilities.KINETIC_DISCHARGER.get(), true);
            }
            else {
                AbilityHelper.disableAbility(player, CybAbilities.KINETIC_DISCHARGER.get(), true);
            }
        });
        return true;
    }
}

