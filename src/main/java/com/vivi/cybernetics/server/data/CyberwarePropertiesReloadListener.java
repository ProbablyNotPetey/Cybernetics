package com.vivi.cybernetics.server.data;

import com.google.gson.*;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.s2c.S2CSyncCyberwarePropertiesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class CyberwarePropertiesReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static final CyberwarePropertiesReloadListener INSTANCE = new CyberwarePropertiesReloadListener();

    private Map<ResourceLocation, CyberwareProperties> properties = new HashMap<>();

    private CyberwarePropertiesReloadListener() {
        super(GSON, "cyberware_properties");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        properties.clear();
        objects.forEach((location, element) -> properties.put(location, CyberwareProperties.deserialize(element)));
    }

    public void fromPacket(Map<ResourceLocation, CyberwareProperties> properties) {
        this.properties = properties;
    }

    public void syncProperties(ServerPlayer player) {
        CybPackets.sendToClient(new S2CSyncCyberwarePropertiesPacket(properties), player);
    }

    public Map<ResourceLocation, CyberwareProperties> getProperties() {
        return properties;
    }
}
