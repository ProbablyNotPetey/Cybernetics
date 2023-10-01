package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.block.entity.SurgicalChamberBlockEntity;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModCapabilities;
import com.vivi.cybernetics.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class SurgicalChamberCyberwareMenu extends CyberwareMenu  {

    private final SurgicalChamberBlockEntity blockEntity;

    public SurgicalChamberCyberwareMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (SurgicalChamberBlockEntity) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public SurgicalChamberCyberwareMenu(int id, Inventory inv, SurgicalChamberBlockEntity be) {
        this(id, inv, be.getCapability(ModCapabilities.CYBERWARE).orElse(null), be);
    }
    public SurgicalChamberCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware, SurgicalChamberBlockEntity be) {
        super(ModMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);
        this.blockEntity = be.getMainBlockEntity();

        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {
                Cybernetics.LOGGER.info("Player cyberware: " + inventory.player.getCapability(ModCapabilities.CYBERWARE).orElse(null).serializeNBT().getAsString());
                Cybernetics.LOGGER.info("Surgical chamber cyberware: " + ((CyberwareMenu) pContainerToSend).getCyberware().serializeNBT().getAsString());
            }

            @Override
            public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {

            }
        });
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if(player instanceof ServerPlayer) {
            CyberwareInventory playerCyberware = player.getCapability(ModCapabilities.CYBERWARE).orElse(null);
            if(!playerCyberware.equals(cyberware)) {
                playerCyberware.copyFrom(cyberware, player, false);
            }
            cyberware.clear();
            if(blockEntity != null) {
                blockEntity.setInUse(false);
            }
        }
    }


}
