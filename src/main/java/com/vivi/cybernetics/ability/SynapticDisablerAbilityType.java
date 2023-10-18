package com.vivi.cybernetics.ability;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.world.entity.player.Player;

public class SynapticDisablerAbilityType extends AbilityType {

    public SynapticDisablerAbilityType() {
        super(100);
    }

    @Override
    public void onEnable(Ability ability, Player player) {
        super.onEnable(ability, player);
        ability.disable(player);
        ability.setCooldown(maxCooldown);
        if(player.level.isClientSide) return;
        //do stuff;
        //todo: implement
    }
}
