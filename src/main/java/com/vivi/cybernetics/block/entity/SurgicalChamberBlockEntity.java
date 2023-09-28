package com.vivi.cybernetics.block.entity;

import com.vivi.cybernetics.block.SurgicalChamberBlock;
import com.vivi.cybernetics.capability.PlayerCyberwareProvider;
import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import com.vivi.cybernetics.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SurgicalChamberBlockEntity extends BlockEntity implements MenuProvider {

    private CyberwareInventory inventory = CyberwareInventory.create();
    private LazyOptional<CyberwareInventory> lazyCyberware = LazyOptional.empty();
    private final boolean isMain;

    public SurgicalChamberBlockEntity(BlockPos pPos, BlockState state) {
        super(ModBlocks.SURGICAL_CHAMBER_BLOCK_ENTITY.get(), pPos, state);
        isMain = state.getValue(SurgicalChamberBlock.PART) == BedPart.HEAD;
    }


    public void tick() {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(!isMain) return;
        lazyCyberware = LazyOptional.of(() -> inventory);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(!isMain) {
            SurgicalChamberBlockEntity main = getMainBlockEntity();
            if(main == null) return super.getCapability(cap, side);
            return getMainBlockEntity().getCapability(cap, side);
        }

        if(cap == PlayerCyberwareProvider.PLAYER_CYBERWARE) {
            return lazyCyberware.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if(!isMain) return;
        lazyCyberware.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Surgical Chamber");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SurgicalChamberCyberwareMenu(pContainerId, pPlayerInventory, inventory);
    }

    public SurgicalChamberBlockEntity getMainBlockEntity() {
        if(isMain) return this;
        BlockPos mainPos = getBlockPos().relative(SurgicalChamberBlock.getNeighbourDirection(BedPart.FOOT, getBlockState().getValue(SurgicalChamberBlock.FACING)));
        BlockState mainState = level.getBlockState(mainPos);
        if(mainState.is(ModBlocks.SURGICAL_CHAMBER_BLOCK.get()) && mainState.getValue(SurgicalChamberBlock.PART) == BedPart.HEAD) {
            return (SurgicalChamberBlockEntity) level.getBlockEntity(mainPos);
        }
        return null;
    }
}
