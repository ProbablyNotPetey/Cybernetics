package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.registry.ModCyberware;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSwitchActiveSlotPacket extends Packet {

    private CyberwareSectionType section;

    public C2SSwitchActiveSlotPacket(CyberwareSectionType section) {
        this.section = section;
    }

    public C2SSwitchActiveSlotPacket(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        section = ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getValue(id);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getKey(section));
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
