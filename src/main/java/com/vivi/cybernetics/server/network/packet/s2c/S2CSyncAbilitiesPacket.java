package com.vivi.cybernetics.server.network.packet.s2c;

import com.vivi.cybernetics.common.ability.AbilityType;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.server.network.ClientPacketHandler;
import com.vivi.cybernetics.server.network.packet.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class S2CSyncAbilitiesPacket extends Packet {

    private CompoundTag abilities;
    private int ownerId;
    private List<ResourceLocation> abilitiesToEnable;
    private List<ResourceLocation> abilitiesToDisable;

    public S2CSyncAbilitiesPacket(Player owner, PlayerAbilities abilities) {
        this(owner, abilities, List.of(), List.of());
    }
    public S2CSyncAbilitiesPacket(Player owner, PlayerAbilities abilities, List<AbilityType> abilitiesToEnable, List<AbilityType> abilitiesToDisable) {
        this.ownerId = owner.getId();
        this.abilities = abilities.serializeNBT();
        this.abilitiesToEnable = abilitiesToEnable.stream().map(type -> CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type)).toList();
        this.abilitiesToDisable = abilitiesToDisable.stream().map(type -> CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type)).toList();
    }

    public S2CSyncAbilitiesPacket(FriendlyByteBuf buf) {
        this.ownerId = buf.readVarInt();
        this.abilities = buf.readNbt();
        this.abilitiesToEnable = buf.readList(FriendlyByteBuf::readResourceLocation);
        this.abilitiesToDisable = buf.readList(FriendlyByteBuf::readResourceLocation);
    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(ownerId);
        buf.writeNbt(abilities);
        buf.writeCollection(abilitiesToEnable, FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(abilitiesToDisable, FriendlyByteBuf::writeResourceLocation);
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

    public int getOwnerId() {
        return ownerId;
    }

    public List<ResourceLocation> getAbilitiesToEnable() {
        return abilitiesToEnable;
    }

    public List<ResourceLocation> getAbilitiesToDisable() {
        return abilitiesToDisable;
    }
}
