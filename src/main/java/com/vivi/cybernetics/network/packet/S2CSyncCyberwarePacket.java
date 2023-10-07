package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import net.minecraft.client.Minecraft;
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
    public S2CSyncCyberwarePacket(Player owner, CyberwareInventory cyberwareInventory) {
        this.cyberwareInventory = cyberwareInventory.serializeNBT();
        this.ownerId = owner.getId();
    }

    public S2CSyncCyberwarePacket(FriendlyByteBuf buf) {
        ownerId = buf.readVarInt();
//        cyberwareInventory = CyberwareInventory.create(player);
        cyberwareInventory = buf.readNbt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(ownerId);
        buf.writeNbt(cyberwareInventory);
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
}
