package com.vivi.cybernetics.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCyberwareProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<CyberwareInventory> PLAYER_CYBERWARE = CapabilityManager.get(new CapabilityToken<>() { });

    private CyberwareInventory container = null;
    private final LazyOptional<CyberwareInventory> lazyContainer = LazyOptional.of(this::getOrCreate);

    private CyberwareInventory getOrCreate() {
        if(container == null) {
            container = new CyberwareInventory();
        }

        return container;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_CYBERWARE) {
            return lazyContainer.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        getOrCreate().saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        getOrCreate().loadNBT(tag);
    }
}
