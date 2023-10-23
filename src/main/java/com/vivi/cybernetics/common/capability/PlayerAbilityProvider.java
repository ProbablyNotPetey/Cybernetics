package com.vivi.cybernetics.common.capability;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAbilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private PlayerAbilities playerAbilities = null;
    private final LazyOptional<PlayerAbilities> lazyAbilities = LazyOptional.of(this::getOrCreate);
    private final Player owner;
    public PlayerAbilityProvider(Player owner) {
        this.owner = owner;
    }

    private PlayerAbilities getOrCreate() {
        if(playerAbilities == null) {
            playerAbilities = new PlayerAbilities(owner);
        }
        return playerAbilities;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == Cybernetics.PLAYER_ABILITIES) {
            return lazyAbilities.cast();
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
