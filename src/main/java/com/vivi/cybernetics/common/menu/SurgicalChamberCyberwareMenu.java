package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.block.entity.SurgicalChamberBlockEntity;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.registry.CybMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class SurgicalChamberCyberwareMenu extends CyberwareMenu {
    private final SurgicalChamberBlockEntity blockEntity;

    public SurgicalChamberCyberwareMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (SurgicalChamberBlockEntity) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public SurgicalChamberCyberwareMenu(int id, Inventory inv, SurgicalChamberBlockEntity be) {
        this(id, inv, be.getCapability(Cybernetics.CYBERWARE).orElse(null), be);
    }
    public SurgicalChamberCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware, SurgicalChamberBlockEntity be) {
        super(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);
        this.blockEntity = be.getMainBlockEntity();
    }
}
