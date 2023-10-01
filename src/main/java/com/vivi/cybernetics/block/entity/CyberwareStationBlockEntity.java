package com.vivi.cybernetics.block.entity;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.util.ModEnergyStorage;
import com.vivi.cybernetics.util.WrappedHandler;
import com.vivi.cybernetics.block.CyberwareStationBlock;
import com.vivi.cybernetics.menu.CyberwareStationMenu;
import com.vivi.cybernetics.recipe.CyberwareStationRecipe;
import com.vivi.cybernetics.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class CyberwareStationBlockEntity extends BlockEntity implements MenuProvider {

    private static final int RATE = 1024;

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if(slot == 7) return false;
            return super.isItemValid(slot, stack);
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> slot == 0 && itemHandler.isItemValid(slot, stack))), //insert if slot is 0
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> (slot > 0 && slot < 7) && itemHandler.isItemValid(slot, stack))), //insert if slot is 1-6
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> (slot > 0 && slot < 7) && itemHandler.isItemValid(slot, stack))), //insert if slot is 1-6
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> (slot > 0 && slot < 7) && itemHandler.isItemValid(slot, stack))), //insert if slot is 1-6
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> (slot > 0 && slot < 7) && itemHandler.isItemValid(slot, stack))), //insert if slot is 1-6
                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (slot) -> slot == 7, //extract if slot is 7
                            (slot, stack) -> slot == 0 && itemHandler.isItemValid(slot, stack))) //insert if slot is 0
            );

    private final ModEnergyStorage energyHandler = new ModEnergyStorage(64000, 4096) {
        @Override
        public void onEnergyChanged() {
            setChanged();

        }
    };
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();



    //data
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;



    public CyberwareStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.CYBERWARE_STATION_BLOCK_ENTITY.get(), pPos, pBlockState);
        //data to be sent to the client screen
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CyberwareStationBlockEntity.this.progress;
                    case 1 -> CyberwareStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CyberwareStationBlockEntity.this.progress = value;
                    case 1 -> CyberwareStationBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }



    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + Cybernetics.MOD_ID + ".cyberware_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CyberwareStationMenu(id, inv, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(CyberwareStationBlock.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    case NORTH -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                    default -> directionWrappedHandlerMap.get(side).cast();
                };
            }
        }

        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }


        return super.getCapability(cap, side);


    }



    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    //nbt

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.put("energy", energyHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        energyHandler.deserializeNBT(tag.getCompound("energy"));
    }



    public void dropContents() {

        Containers.dropContents(this.level, this.worldPosition, createContainerFromHandler());
    }



    public static void tick(Level level, BlockPos pos, BlockState state, CyberwareStationBlockEntity blockEntity) {
        if(level.isClientSide) return;

        //pseudocode
        if(blockEntity.hasRecipe()) {
            blockEntity.progress++;
            blockEntity.energyHandler.extractEnergy(RATE, false);


            state = state.setValue(CyberwareStationBlock.POWERED, true);
            setChanged(level, pos, state);

            if(blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.craft();
            }
        }
        else {
            blockEntity.resetProgress();
            state = state.setValue(CyberwareStationBlock.POWERED, false);
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craft() {

        SimpleContainer container = createContainerFromHandler();

        Optional<CyberwareStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CyberwareStationRecipe.Type.INSTANCE, container, level);

        if(hasRecipe()) {

            for(int i = 0; i < 7; i++) {
                itemHandler.extractItem(i, 1, false);
            }
            ItemStack output = recipe.get().getResultItem();
            output.grow(itemHandler.getStackInSlot(7).getCount());
            itemHandler.setStackInSlot(7, output);
            resetProgress();
        }

    }

    private boolean hasRecipe() {

        SimpleContainer container = createContainerFromHandler();

        Optional<CyberwareStationRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CyberwareStationRecipe.Type.INSTANCE, container, level);

        if(recipe.isEmpty()) return false;
        if(energyHandler.getEnergyStored() == 0) return false;

        boolean canInsertIntoOutput = container.getItem(7).getItem() == recipe.get().getResultItem().getItem() || container.getItem(7).isEmpty();
        boolean canInsertAmountIntoOutput = container.getItem(7).getMaxStackSize() >= container.getItem(7).getCount() + recipe.get().getResultItem().getCount();

        return canInsertIntoOutput && canInsertAmountIntoOutput;

    }

    private SimpleContainer createContainerFromHandler() {
        SimpleContainer out = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            out.setItem(i, itemHandler.getStackInSlot(i));
        }
        return out;
    }


}
