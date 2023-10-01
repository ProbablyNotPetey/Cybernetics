package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static Capability<CyberwareInventory> PLAYER_CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });

}
