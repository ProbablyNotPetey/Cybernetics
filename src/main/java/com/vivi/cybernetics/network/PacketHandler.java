package com.vivi.cybernetics.network;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.network.packet.C2SOpenCyberwarePacket;
import com.vivi.cybernetics.network.packet.C2SSwitchActiveSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel network = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Cybernetics.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = network;

        network.messageBuilder(C2SSwitchActiveSlotPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSwitchActiveSlotPacket::new)
                .encoder(C2SSwitchActiveSlotPacket::toBytes)
                .consumerMainThread(C2SSwitchActiveSlotPacket::handle)
                .add();

        network.messageBuilder(C2SOpenCyberwarePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SOpenCyberwarePacket::new)
                .encoder(C2SOpenCyberwarePacket::toBytes)
                .consumerMainThread(C2SOpenCyberwarePacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}