package com.vivi.cybernetics.common.capability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.cyberware.CyberwareInventory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCyberwareProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private CyberwareInventory cyberwareInventory = null;
    private final LazyOptional<CyberwareInventory> lazyContainer = LazyOptional.of(this::getOrCreate);
    private final Player owner;
    public PlayerCyberwareProvider(Player owner) {
        this.owner = owner;
    }
    private CyberwareInventory getOrCreate() {
        if(cyberwareInventory == null) {
            cyberwareInventory = CyberwareInventory.create(owner);
        }

        return cyberwareInventory;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == Cybernetics.CYBERWARE) {
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
