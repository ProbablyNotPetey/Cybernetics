package com.vivi.cybernetics.common.capability.provider;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.capability.PlayerSpike;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSpikeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private PlayerSpike playerSpike = null;
    private final LazyOptional<PlayerSpike> lazySpike = LazyOptional.of(this::getOrCreate);
    private PlayerSpike getOrCreate() {
        if(playerSpike == null) {
            playerSpike = new PlayerSpike();
        }
        return playerSpike;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == Cybernetics.PLAYER_SPIKE) {
            return lazySpike.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreate().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreate().deserializeNBT(nbt);
    }
}
