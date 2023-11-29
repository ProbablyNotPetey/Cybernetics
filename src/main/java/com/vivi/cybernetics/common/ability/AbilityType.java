package com.vivi.cybernetics.common.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

//This should be a registry
public class AbilityType {

    protected final int maxCooldown;
    protected final ResourceLocation texture;
    public AbilityType() {
        this(-1, null);
    }
    public AbilityType(int maxCooldown) {
        this(maxCooldown, null);
    }
    public AbilityType(ResourceLocation texture) {
        this(-1, texture);
    }
    public AbilityType(int maxCooldown, ResourceLocation texture) {
        this.maxCooldown = maxCooldown;
        this.texture = texture;
    }


    public void onEnable(Ability ability, Level level, Player player) {

    }
    public void tick(Ability ability, Level level, Player player) {

    }
    public void onDisable(Ability ability, Level level, Player player) {
        if(maxCooldown > -1) {
            ability.setCooldown(maxCooldown);
        }
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public ResourceLocation getTexture() {
//        if(itemToRender == null) return null;
        return texture;
    }
}
