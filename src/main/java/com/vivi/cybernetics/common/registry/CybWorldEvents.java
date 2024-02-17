package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.common.worldevent.SynapticDisablerWorldEvent;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class CybWorldEvents {
    public static final WorldEventType
            SYNAPTIC_DISABLER = LodestoneWorldEventTypeRegistry.registerEventType(new WorldEventType("synaptic_disabler", SynapticDisablerWorldEvent::new));
}
