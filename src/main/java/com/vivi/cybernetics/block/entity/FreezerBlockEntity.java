package com.vivi.cybernetics.block.entity;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.block.FreezerBlock;
import com.vivi.cybernetics.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FreezerBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            FreezerBlockEntity.this.playSound(state, SoundEvents.IRON_DOOR_OPEN);
            FreezerBlockEntity.this.updateBlockState(state, true);
        }

        protected void onClose(Level level, BlockPos pos, BlockState state) {
            FreezerBlockEntity.this.playSound(state, SoundEvents.IRON_DOOR_CLOSE);
            FreezerBlockEntity.this.updateBlockState(state, false);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int newCount, int oldCount) {
        }

        protected boolean isOwnContainer(Player p_155060_) {
            if (p_155060_.containerMenu instanceof ChestMenu) {
                Container container = ((ChestMenu)p_155060_.containerMenu).getContainer();
                return container == FreezerBlockEntity.this;
            } else {
                return false;
            }
        }
    };



    public FreezerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.FREEZER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacksIn) {
        inventory = stacksIn;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + Cybernetics.MOD_ID + ".freezer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return ChestMenu.threeRows(id, playerInv, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if(!trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, inventory);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if(!tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, inventory);
    }

    public void startOpen(Player player) {
        if (level != null && !this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (level != null && !this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (level != null && !this.remove) {
            this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
        }
    }

    private void updateBlockState(BlockState state, boolean open) {
        if(level != null) {
            this.level.setBlock(this.getBlockPos(), state.setValue(FreezerBlock.OPEN, Boolean.valueOf(open)), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if(level == null) return;

        Vec3i facingVector = state.getValue(FreezerBlock.FACING).getNormal();
        double x = (double)this.worldPosition.getX() + 0.5D + (double)facingVector.getX() / 2.0D;
        double y = (double)this.worldPosition.getY() + 0.5D + (double)facingVector.getY() / 2.0D;
        double z = (double)this.worldPosition.getZ() + 0.5D + (double)facingVector.getZ() / 2.0D;
        this.level.playSound((Player)null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);

    }


}
