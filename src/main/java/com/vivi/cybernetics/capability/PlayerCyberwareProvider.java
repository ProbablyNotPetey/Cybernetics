package com.vivi.cybernetics.capability;

import com.vivi.cybernetics.cyberware.CyberwareInventory;
import com.vivi.cybernetics.registry.ModCyberware;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCyberwareProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private CyberwareInventory cyberwareInventory = null;
    private final LazyOptional<CyberwareInventory> lazyContainer = LazyOptional.of(this::getOrCreate);

    private CyberwareInventory getOrCreate() {
        if(cyberwareInventory == null) {
            cyberwareInventory = CyberwareInventory.create();
        }

        return cyberwareInventory;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModCyberware.CYBERWARE) {
            return lazyContainer.cast();
        }
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
