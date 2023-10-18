package com.vivi.cybernetics.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.ability.Ability;
import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.capability.PlayerAbilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public class AbilityHelper {

    public static void addAbility(Player player, AbilityType type) {
        addAbility(player, type, false);
    }
    public static void addAbility(Player player, AbilityType type, boolean syncToClient) {
        getAbilities(player).ifPresent(abilities -> {
            if(!abilities.hasAbility(type)) {
                abilities.addAbility(new Ability(type), syncToClient);
            }
        });
    }
    public static void removeAbility(Player player, AbilityType type) {
        removeAbility(player, type, false);
    }
    public static void removeAbility(Player player, AbilityType type, boolean syncToClient) {
        getAbilities(player).ifPresent(abilities -> {
            for (int i = 0; i < abilities.getAbilities().size(); i++) {
                Ability ability = abilities.getAbilities().get(i);
                if (ability.getType() == type) {
                    abilities.removeAbility(ability, syncToClient);
                    i--;
                }
            }
        });
    }

    public static LazyOptional<PlayerAbilities> getAbilities(Player player) {
        if(player == null) return LazyOptional.empty();
        return player.getCapability(Cybernetics.PLAYER_ABILITIES);
    }

    public static void enableAbility(Player player, AbilityType type) {
        enableAbility(player, type, false);
    }
    public static void enableAbility(Player player, AbilityType type, boolean syncToClient) {
        getAbilities(player).ifPresent(abilities -> {
            abilities.enableAbility(type, syncToClient);
        });
    }
    public static void disableAbility(Player player, AbilityType type) {
        disableAbility(player, type, false);
    }
    public static void disableAbility(Player player, AbilityType type, boolean syncToClient) {
        getAbilities(player).ifPresent(abilities -> {
            abilities.disableAbility(type, syncToClient);
        });
    }

}
