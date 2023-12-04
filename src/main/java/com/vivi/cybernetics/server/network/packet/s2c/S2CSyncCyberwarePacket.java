package com.vivi.cybernetics.server.network.packet.s2c;

import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.server.network.ClientPacketHandler;
import com.vivi.cybernetics.server.network.packet.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSyncCyberwarePacket extends Packet {

//    private CyberwareInventory cyberwareInventory;
    private CompoundTag cyberwareInventory;
    private int ownerId;
    private boolean update;
    public S2CSyncCyberwarePacket(Player owner, CyberwareInventory cyberwareInventory, boolean update) {
        this.cyberwareInventory = cyberwareInventory.serializeNBT();
        this.ownerId = owner.getId();
        this.update = update;
    }

    public S2CSyncCyberwarePacket(FriendlyByteBuf buf) {
        ownerId = buf.readVarInt();
//        cyberwareInventory = CyberwareInventory.create(player);
        cyberwareInventory = buf.readNbt();
        update = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(ownerId);
        buf.writeNbt(cyberwareInventory);
        buf.writeBoolean(update);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSyncCyberwarePacket(ctx, this));
        });
        return true;
    }

    public CompoundTag getCyberwareData() {
        return cyberwareInventory;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public boolean shouldUpdate() {
        return update;
    }
}
