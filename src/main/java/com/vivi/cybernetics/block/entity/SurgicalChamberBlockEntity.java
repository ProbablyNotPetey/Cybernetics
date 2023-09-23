package com.vivi.cybernetics.block.entity;

import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import com.vivi.cybernetics.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SurgicalChamberBlockEntity extends BlockEntity {
    public SurgicalChamberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.SURGICAL_CHAMBER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    public void tick() {

    }
}
