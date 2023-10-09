package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.registry.CybCyberware;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSwitchActiveSlotPacket extends Packet {

    private CyberwareSectionType section;

    public C2SSwitchActiveSlotPacket(CyberwareSectionType section) {
        this.section = section;
    }
    public C2SSwitchActiveSlotPacket() {
        this.section = null;
    }

    public C2SSwitchActiveSlotPacket(FriendlyByteBuf buf) {
        boolean isNull = buf.readBoolean();
        if(!isNull) {
            ResourceLocation id = buf.readResourceLocation();
            section = CybCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getValue(id);
        }
        else {
            section = null;
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(section == null);
        if(section != null) {
            buf.writeResourceLocation(CybCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getKey(section));
        }
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player.containerMenu instanceof CyberwareMenu menu) {
                menu.switchActiveSlots(section);
            }
        });
        return true;
    }
}
