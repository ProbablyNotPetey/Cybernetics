package com.vivi.cybernetics.capability;

import com.vivi.cybernetics.ability.Ability;
import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.network.CybPackets;
import com.vivi.cybernetics.network.packet.S2CSyncAbilitiesPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class PlayerAbilities implements INBTSerializable<CompoundTag> {

    private List<Ability> abilities = new ArrayList<>();
    private final Player player;
    public PlayerAbilities(Player player) {
        this.player = player;
    }

    public void copyFrom(PlayerAbilities oldStore) {
        abilities.clear();
        abilities.addAll(oldStore.getAbilities());
        if(!player.level.isClientSide) {
            syncToClient((ServerPlayer) player);
        }
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
        if(!player.level.isClientSide) {
            syncToClient((ServerPlayer) player);
        }
    }

    public void removeAbility(Ability ability) {
        abilities.remove(ability);
        if(!player.level.isClientSide) {
            syncToClient((ServerPlayer) player);
        }
    }

    public boolean hasAbility(AbilityType type) {
        for (Ability ability : abilities) {
            if (ability.getType() == type) return true;
        }
        return false;
    }
    public Ability getAbility(AbilityType type) {
        for(Ability ability : abilities) {
            if(ability.getType() == type) return ability;
        }
        return null;
    }

    public void tickAbilities() {
        abilities.forEach(ability -> {
            ability.tick(player);
        });
    }

    public void enableAbility(AbilityType type) {
        Ability ability = getAbility(type);
        if(ability != null) ability.enable(player);
        if(!player.level.isClientSide) {
            syncToClient((ServerPlayer) player);
        }
    }

    public void disableAbility(AbilityType type) {
        Ability ability = getAbility(type);
        if(ability != null) ability.disable(player);
        if(!player.level.isClientSide) {
            syncToClient((ServerPlayer) player);
        }
    }


    public void syncToClient(ServerPlayer client) {
        CybPackets.sendToClient(new S2CSyncAbilitiesPacket(player, this), client);
    }



    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for(Ability ability : abilities) {
            list.add(ability.serializeNBT());
        }
        tag.put("abilities", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        abilities.clear();
        ListTag list = tag.getList("abilities", Tag.TAG_COMPOUND);
        list.forEach(abilityTag -> {
            Ability ability = new Ability((CompoundTag) abilityTag);
            abilities.add(ability);
        });
    }
}
