package com.vivi.cybernetics.common.capability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.ability.Ability;
import com.vivi.cybernetics.common.ability.AbilityType;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.s2c.S2CSyncAbilitiesPacket;
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
//        player = oldStore.player;
//        if(!player.level.isClientSide) {
//            syncToClient((ServerPlayer) player);
//        }
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbility(Ability ability, boolean syncToClient) {
        abilities.add(ability);

        if(!player.level.isClientSide && syncToClient) {
            syncToClient((ServerPlayer) player);
        }
    }

    public void removeAbility(Ability ability, boolean syncToClient) {
        if(ability.isEnabled()) {
            ability.disable(player);
        }
        abilities.remove(ability);
        if(!player.level.isClientSide && syncToClient) {
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

    public boolean enableAbility(AbilityType type, boolean syncToClient) {
        Ability ability = getAbility(type);
        if(ability == null) return false;
        Cybernetics.LOGGER.info("Ability not null");
        if(!ability.enable(player)) return false;
        if(!player.level.isClientSide && syncToClient) {
            Cybernetics.LOGGER.info("Syncing abilities to client, ability to enable: " + CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type));
            syncToClient((ServerPlayer) player, List.of(type), List.of());
        }
        return true;
    }

    public boolean disableAbility(AbilityType type, boolean syncToClient) {
        Ability ability = getAbility(type);
        if(ability == null) return false;
        if(!ability.disable(player)) return false;
        if(!player.level.isClientSide && syncToClient) {
            syncToClient((ServerPlayer) player, List.of(), List.of(type));
        }
        return true;
    }


    public void syncToClient(ServerPlayer client) {
        syncToClient(client, List.of(), List.of());
    }
    public void syncToClient(ServerPlayer client, List<AbilityType> abilitiesToEnable, List<AbilityType> abilitiesToDisable) {
//        Cybernetics.LOGGER.info("Syncing abilities to client");
//        Cybernetics.LOGGER.info("passed in client id: " + client.getId() + ", stored player id: " + player.getId());
        CybPackets.sendToClient(new S2CSyncAbilitiesPacket(player, this, abilitiesToEnable, abilitiesToDisable), client);
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
