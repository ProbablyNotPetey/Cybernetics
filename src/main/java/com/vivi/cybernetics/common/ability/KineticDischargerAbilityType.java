package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.item.KineticDischargerItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


@HiddenAbility
public class KineticDischargerAbilityType extends AbilityType {

    public KineticDischargerAbilityType() {
        super(100);
    }

    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        KineticDischargerItem.spike(player);
    }
}
