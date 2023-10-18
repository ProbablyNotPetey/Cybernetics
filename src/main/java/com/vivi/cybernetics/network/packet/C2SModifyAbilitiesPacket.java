package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.registry.CybAbilities;
import com.vivi.cybernetics.util.AbilityHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SModifyAbilitiesPacket extends Packet {

    private final AbilityType type;
    private final OpCode operation;
    public C2SModifyAbilitiesPacket(AbilityType type, OpCode operation) {
        this.type = type;
        this.operation = operation;
    }

    public C2SModifyAbilitiesPacket(FriendlyByteBuf buf) {
        type = CybAbilities.ABILITY_TYPE_REGISTRY.get().getValue(buf.readResourceLocation());
        operation = OpCode.values()[buf.readVarInt()];
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        ResourceLocation rLoc = CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type);
        if(rLoc == null) throw new IllegalArgumentException("ability type not registered!");
        buf.writeResourceLocation(rLoc);
        buf.writeVarInt(operation.ordinal());

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            switch (operation) {
                case ENABLE -> AbilityHelper.enableAbility(player, type);
                case DISABLE -> AbilityHelper.disableAbility(player, type);
                case ADD -> AbilityHelper.addAbility(player, type);
                case REMOVE -> AbilityHelper.removeAbility(player, type);
            }
        });
        return true;
    }

    public enum OpCode {
        ENABLE,
        DISABLE,
        ADD,
        REMOVE
    }
}
