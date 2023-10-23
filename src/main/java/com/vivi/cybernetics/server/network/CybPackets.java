package com.vivi.cybernetics.server.network;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.server.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CybPackets {
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

        //C2S

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

        network.messageBuilder(C2SDoubleJumpPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SDoubleJumpPacket::new)
                .encoder(C2SDoubleJumpPacket::toBytes)
                .consumerMainThread(C2SDoubleJumpPacket::handle)
                .add();

        network.messageBuilder(C2SSpikePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSpikePacket::new)
                .encoder(C2SSpikePacket::toBytes)
                .consumerMainThread(C2SSpikePacket::handle)
                .add();

        network.messageBuilder(C2SSpikeShockwavePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSpikeShockwavePacket::new)
                .encoder(C2SSpikeShockwavePacket::toBytes)
                .consumerMainThread(C2SSpikeShockwavePacket::handle)
                .add();

        network.messageBuilder(C2SModifyAbilitiesPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SModifyAbilitiesPacket::new)
                .encoder(C2SModifyAbilitiesPacket::toBytes)
                .consumerMainThread(C2SModifyAbilitiesPacket::handle)
                .add();

        //S2C

        network.messageBuilder(S2CSyncCyberwarePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CSyncCyberwarePacket::new)
                .encoder(S2CSyncCyberwarePacket::toBytes)
                .consumerMainThread(S2CSyncCyberwarePacket::handle)
                .add();

        network.messageBuilder(S2CSyncAbilitiesPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CSyncAbilitiesPacket::new)
                .encoder(S2CSyncAbilitiesPacket::toBytes)
                .consumerMainThread(S2CSyncAbilitiesPacket::handle)
                .add();

        network.messageBuilder(S2CSyncCyberwarePropertiesPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CSyncCyberwarePropertiesPacket::new)
                .encoder(S2CSyncCyberwarePropertiesPacket::toBytes)
                .consumerMainThread(S2CSyncCyberwarePropertiesPacket::handle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}