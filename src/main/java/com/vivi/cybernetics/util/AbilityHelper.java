package com.vivi.cybernetics.util;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.ability.Ability;
import com.vivi.cybernetics.ability.AbilityType;
import com.vivi.cybernetics.capability.PlayerAbilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public class AbilityHelper {

    public static void addAbility(Player player, AbilityType type) {
        getAbilities(player).ifPresent(abilities -> {
            if(!abilities.hasAbility(type)) {
                abilities.addAbility(new Ability(type));
            }
        });
    }
    public static void removeAbility(Player player, AbilityType type) {
        getAbilities(player).ifPresent(abilities -> {
            for (Ability ability : abilities.getAbilities()) {
                if (ability.getType() == type) {
                    abilities.removeAbility(ability);
                }
            }
        });
    }

    public static LazyOptional<PlayerAbilities> getAbilities(Player player) {
        return player.getCapability(Cybernetics.PLAYER_ABILITIES);
    }

}
