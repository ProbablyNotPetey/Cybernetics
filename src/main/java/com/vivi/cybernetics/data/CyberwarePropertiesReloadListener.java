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
            boolean showRequirements = true;
            if(object.has("show_requirements")) {
                showRequirements = object.get("show_requirements").getAsBoolean();
            }
            boolean showIncompatibilities = true;
            if(object.has("show_incompatibilities")) {
                showIncompatibilities = object.get("show_incompatibilities").getAsBoolean();
            }
            boolean showDescription = true;
            if(object.has("show_description")) {
                showDescription = object.get("show_description").getAsBoolean();
            }
            int capacity = 0;
            if(object.has("capacity")) {
                capacity = object.get("capacity").getAsInt();
            }
            properties.put(location, CyberwareProperties.fromStringList(strRequirements, strIncompatibilites, showRequirements, showIncompatibilities, showDescription, capacity));
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
