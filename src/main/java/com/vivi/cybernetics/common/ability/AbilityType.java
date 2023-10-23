package com.vivi.cybernetics.common.ability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

//This should be a registry
public class AbilityType {

    protected final int maxCooldown;
    protected final Item itemToRender;
    public AbilityType() {
        this(-1, null);
    }
    public AbilityType(int maxCooldown) {
        this(maxCooldown, null);
    }
    public AbilityType(Item itemToRender) {
        this(-1, itemToRender);
    }
    public AbilityType(int maxCooldown, Item itemToRender) {
        this.maxCooldown = maxCooldown;
        this.itemToRender = itemToRender;
    }


    public void onEnable(Ability ability, Level level, Player player) {

    }
    public void tick(Ability ability, Level level, Player player) {

    }
    public void onDisable(Ability ability, Level level, Player player) {

    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public Item getItemToRender() {
//        if(itemToRender == null) return null;
        return itemToRender;
    }
}
