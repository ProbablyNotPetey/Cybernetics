package com.vivi.cybernetics.common.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.block.entity.SurgicalChamberBlockEntity;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.registry.CybMenuTypes;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class SurgicalChamberCyberwareMenu extends CyberwareMenu {
    private final SurgicalChamberBlockEntity blockEntity;

    public SurgicalChamberCyberwareMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (SurgicalChamberBlockEntity) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public SurgicalChamberCyberwareMenu(int id, Inventory inv, SurgicalChamberBlockEntity be) {
        this(id, inv, be.getCapability(Cybernetics.CYBERWARE).orElse(null), be);
    }
    public SurgicalChamberCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware, SurgicalChamberBlockEntity be) {
        super(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware, true);
        this.blockEntity = be.getMainBlockEntity();
    }

    @Override
    public void applyChanges(Player player) {
        super.applyChanges(player);
        if(player instanceof ServerPlayer) {
            CyberwareInventory playerCyberware = CyberwareHelper.getCyberware(player).orElse(null);
            if(!playerCyberware.equals(cyberware)) {
                playerCyberware.copyFrom(cyberware, player, true);
            }
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (pPlayer.level.isClientSide) return;
        cyberware.clear();
        if(blockEntity != null) {
            blockEntity.setInUse(false);
        }
    }
}
