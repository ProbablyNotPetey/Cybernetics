package com.vivi.cybernetics.common.capability;

import com.vivi.cybernetics.common.util.ModEnergyStorage;
import net.minecraft.nbt.CompoundTag;

public class PlayerEnergyStorage extends ModEnergyStorage {
    public PlayerEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public void onEnergyChanged() {

    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy", energy);
        tag.putInt("max_energy", capacity);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        energy = tag.getInt("energy");
        capacity = tag.getInt("max_energy");
    }

    public void copyFrom(PlayerEnergyStorage other) {
        this.energy = other.energy;
        this.capacity = other.capacity;
    }
}
