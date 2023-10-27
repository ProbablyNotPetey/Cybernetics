package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.server.network.ClientPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class S2CSyncAbilitiesPacket extends Packet {

    private CompoundTag abilities;
    private UUID ownerId;
    public S2CSyncAbilitiesPacket(Player owner, PlayerAbilities abilities) {
        this.ownerId = owner.getUUID();
        this.abilities = abilities.serializeNBT();
    }

    public S2CSyncAbilitiesPacket(FriendlyByteBuf buf) {
        this.ownerId = buf.readUUID();
        this.abilities = buf.readNbt();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(ownerId);
        buf.writeNbt(abilities);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSyncAbiilitiesPacket(ctx, this));
        });
        return true;
    }

    public CompoundTag getAbilitiesData() {
        return abilities;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}
