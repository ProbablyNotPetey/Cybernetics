package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.server.data.CyberwareProperties;
import com.vivi.cybernetics.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class S2CSyncCyberwarePropertiesPacket extends Packet {

    private Map<ResourceLocation, CyberwareProperties> properties;
    public S2CSyncCyberwarePropertiesPacket(Map<ResourceLocation, CyberwareProperties> properties) {
        this.properties = properties;
    }
    public S2CSyncCyberwarePropertiesPacket(FriendlyByteBuf buf) {
        properties = new HashMap<>();
        int size = buf.readVarInt();
        for(int i = 0; i < size; i++) {
            properties.put(buf.readResourceLocation(), CyberwareProperties.fromNetwork(buf));
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(properties.size());
        properties.forEach((location, properties) -> {
            buf.writeResourceLocation(location);
            properties.toNetwork(buf);
        });
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSyncCyberwarePropertiesPacket(ctx, this));
        });
        return true;
    }

    public Map<ResourceLocation, CyberwareProperties> getProperties() {
        return properties;
    }
}
