package com.vivi.cybernetics.data;

import com.google.gson.*;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.network.packet.S2CSyncCyberwarePropertiesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        objects.forEach((location, element) -> {
            JsonObject object = element.getAsJsonObject();
            List<String> strRequirements = new ArrayList<>();
            if(object.has("requirements")) {
                JsonArray requirements = object.getAsJsonArray("requirements");
                requirements.forEach(str -> {
                    strRequirements.add(str.getAsString());
                });
            }
            List<String> strIncompatibilites = new ArrayList<>();
            if(object.has("incompatibilities")) {
                JsonArray incompatibilities = object.getAsJsonArray("incompatibilities");
                incompatibilities.forEach(str -> {
                    strIncompatibilites.add(str.getAsString());
                });
            }
            properties.put(location, CyberwareProperties.fromStringList(strRequirements, strIncompatibilites));
        });
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
