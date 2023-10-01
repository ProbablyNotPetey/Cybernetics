package com.vivi.cybernetics.menu;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModCapabilities;
import com.vivi.cybernetics.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class SurgicalChamberCyberwareMenu extends CyberwareMenu  {

    public SurgicalChamberCyberwareMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, inv.player.level.getBlockEntity(buf.readBlockPos()).getCapability(ModCapabilities.PLAYER_CYBERWARE).orElse(null));
    }
    public SurgicalChamberCyberwareMenu(int pContainerId, Inventory inventory, CyberwareInventory cyberware) {
        super(ModMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);


        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {
                Cybernetics.LOGGER.info("Player cyberware: " + inventory.player.getCapability(ModCapabilities.PLAYER_CYBERWARE).orElse(null).serializeNBT().getAsString());
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
            CyberwareInventory playerCyberware = player.getCapability(ModCapabilities.PLAYER_CYBERWARE).orElse(null);
            if(!playerCyberware.equals(cyberware)) {
                playerCyberware.copyFrom(cyberware);
            }
        }
    }


}
