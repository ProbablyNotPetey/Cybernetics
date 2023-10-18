package com.vivi.cybernetics.ability;

import net.minecraft.world.entity.player.Player;

//This should be a registry
public class AbilityType {

    protected final int maxCooldown;
    public AbilityType(int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    public AbilityType() {
        this.maxCooldown = -1;
    }


    public void onEnable(Ability ability, Player player) {

    }
    public void tick(Ability ability, Player player) {

    }
    public void onDisable(Ability ability, Player player) {

    }

    public int getMaxCooldown() {
        return maxCooldown;
    }
}
