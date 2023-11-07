package com.vivi.cybernetics.common.menu.deprecated;
/*
import com.mojang.datafixers.util.Pair;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.block.entity.SurgicalChamberBlockEntity;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import com.vivi.cybernetics.common.item.AttributeCyberwareItem;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.menu.CyberwareSlot;
import com.vivi.cybernetics.common.registry.CybAttributes;
import com.vivi.cybernetics.common.registry.CybMenuTypes;
import com.vivi.cybernetics.common.registry.CybTags;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SurgicalChamberCyberwareMenuOld extends CyberwareMenuOld {

    private final SurgicalChamberBlockEntity blockEntity;

    public SurgicalChamberCyberwareMenuOld(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (SurgicalChamberBlockEntity) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public SurgicalChamberCyberwareMenuOld(int id, Inventory inv, SurgicalChamberBlockEntity be) {
        this(id, inv, be.getCapability(Cybernetics.CYBERWARE).orElse(null), be);
    }
    public SurgicalChamberCyberwareMenuOld(int pContainerId, Inventory inventory, CyberwareInventory cyberware, SurgicalChamberBlockEntity be) {
        super(CybMenuTypes.SURGICAL_CHAMBER_CYBERWARE_MENU.get(), pContainerId, inventory, cyberware);
        this.blockEntity = be.getMainBlockEntity();

        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int slot, ItemStack stack) {
                if(!(menu instanceof CyberwareMenuOld cyberwareMenu)) return;
                if(!(cyberwareMenu.getSlot(slot) instanceof CyberwareSlot)) return;
                if(activeSection == null) return; //bad hack but i dont care anymore

//                Cybernetics.LOGGER.info("Stack: " + stack + ", Stack in slot: " + cyberwareMenu.getSlot(slot).getItem() + ", Carried: " + cyberwareMenu.getCarried());
                if(cyberwareMenu.getCarried().is(CybTags.CAPACITY_ITEMS) && stack.isEmpty()) {
                    updateMaxCapacity(cyberwareMenu.getCarried(), true);
                }
                else if(stack.is(CybTags.CAPACITY_ITEMS)) {
                    updateMaxCapacity(stack, false);
                }
                if(cyberwareMenu.getCarried().is(stack.getItem())) return;

//                if(pStack.is(cyberwareMenu.getSlot(slot).getItem().getItem())) return;
//                Cybernetics.LOGGER.info("Is pickup: " + stack.is(cyberwareMenu.getSlot(slot).getItem().getItem()));
                for(int i = 0; i < cyberwareMenu.getCyberware().getSlots(); i++) {
                    if (!(cyberwareMenu.getCyberware().getStackInSlot(i).getItem() instanceof CyberwareItem item) || !(cyberwareMenu.getCarried().getItem() instanceof CyberwareItem)) {
                        continue;
                    }
                    for (Ingredient req : item.getRequirements()) {
                        if (req.test(cyberwareMenu.getCarried())) {
                            SurgicalChamberCyberwareMenuOld.this.moveItemStackTo(cyberwareMenu.getCyberware().getStackInSlot(i), cyberwareMenu.getCyberware().getSlots(), cyberwareMenu.slots.size(), false);
                            break;
                        }
                    }
                }
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
            CyberwareInventory playerCyberware = CyberwareHelper.getCyberware(player).orElse(null);
            if(!playerCyberware.equals(cyberware)) {
                playerCyberware.copyFrom(cyberware, player, true);
            }
            cyberware.clear();
            if(blockEntity != null) {
                blockEntity.setInUse(false);
            }
        }
    }

    //updates max capacity temporarily for surgical chamber cyberware menu
    private void updateMaxCapacity(ItemStack stack, boolean wasPickup) {
        if(!(stack.getItem() instanceof AttributeCyberwareItem item)) return;
        Pair<Attribute, AttributeModifier>[] attributes = item.getAttributes();
        for (Pair<Attribute, AttributeModifier> attribute : attributes) {
            if (attribute.getFirst() != CybAttributes.MAX_CAPACITY.get()) continue;

            AttributeModifier modifier = attribute.getSecond();
            int newValue;
            if (wasPickup) {
                newValue = switch (modifier.getOperation()) {
                    case ADDITION -> (int) (this.maxCapacityData.get() - modifier.getAmount());
                    case MULTIPLY_BASE ->
                            (int) (this.maxCapacityData.get() - (inventory.player.getAttribute(CybAttributes.MAX_CAPACITY.get()).getBaseValue() * modifier.getAmount()));
                    case MULTIPLY_TOTAL -> (int) (this.maxCapacityData.get() / (1 + modifier.getAmount()));
                };
            } else {
                newValue = switch (modifier.getOperation()) {
                    case ADDITION -> (int) (this.maxCapacityData.get() + modifier.getAmount());
                    case MULTIPLY_BASE ->
                            (int) (this.maxCapacityData.get() + (inventory.player.getAttribute(CybAttributes.MAX_CAPACITY.get()).getBaseValue() * modifier.getAmount()));
                    case MULTIPLY_TOTAL -> (int) (this.maxCapacityData.get() * (1 + modifier.getAmount()));
                };
            }
            this.maxCapacityData.set(newValue);
        }

    }


}

 */