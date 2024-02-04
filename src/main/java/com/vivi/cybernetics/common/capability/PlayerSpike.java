package com.vivi.cybernetics.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerSpike implements INBTSerializable<CompoundTag> {

    private int time = -1;
    private boolean isSpiking;
    public PlayerSpike() {

    }

    public void copyFrom(PlayerSpike other) {
        this.time = other.getTime();
        this.isSpiking = other.isSpiking();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("is_spiking", isSpiking);
        tag.putInt("spike_time", time);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        isSpiking = tag.getBoolean("is_spiking");
        time = tag.getInt("spike_time");
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isSpiking() {
        return isSpiking;
    }

    public void setSpiking(boolean spiking) {
        isSpiking = spiking;
    }
}
