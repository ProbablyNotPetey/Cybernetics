package com.vivi.cybernetics.network.packet;

import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.registry.ModCyberware;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class C2SOpenCyberwarePacket extends Packet {

    public C2SOpenCyberwarePacket() {

    }

    public C2SOpenCyberwarePacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            player.getCapability(ModCyberware.CYBERWARE).ifPresent(cyberware -> {
                NetworkHooks.openScreen(player, new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer) -> new PlayerCyberwareMenu(pContainerId, pPlayerInventory, cyberware)), Component.literal(("Cyberware"))));
            });

        });

        return true;
    }
}
