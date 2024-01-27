package com.vivi.cybernetics.server.network.packet.c2s;

import com.vivi.cybernetics.common.ability.Ability;
import com.vivi.cybernetics.common.ability.DashAbilityType;
import com.vivi.cybernetics.common.item.DashCyberwareItem;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.server.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDashPacket extends Packet {

    public C2SDashPacket() {

    }

    public C2SDashPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player == null) return;
            AbilityHelper.getAbilities(player).ifPresent(abilities -> {
                for (Ability ability : abilities.getAbilities()) {
                    if (ability.getType() instanceof DashAbilityType type && type.canDash(ability, player.level(), player)) {
                        AbilityHelper.enableAbility(player, type, true);
                        return;
                    }
                }
            });

        });
        return true;
    }
}
