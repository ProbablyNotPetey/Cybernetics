package com.vivi.cybernetics.common.capability.provider;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.capability.PlayerEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEnergyProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private PlayerEnergyStorage playerEnergyHandler = null;
    private final LazyOptional<PlayerEnergyStorage> lazyPlayerEnergy = LazyOptional.of(this::getOrCreate);


    private PlayerEnergyStorage getOrCreate() {
        if(playerEnergyHandler == null) {
            playerEnergyHandler = new PlayerEnergyStorage(64000, 256);
        }
        return playerEnergyHandler;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == Cybernetics.PLAYER_ENERGY) {
            return lazyPlayerEnergy.cast();
        };
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        getOrCreate().deserializeNBT(tag);
    }
}
