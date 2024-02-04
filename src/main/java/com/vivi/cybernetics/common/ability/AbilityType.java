package com.vivi.cybernetics.common.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

//This should be a registry
public class AbilityType {

    protected final int maxCooldown;
    protected final int duration;
    protected final ResourceLocation texture;
    public AbilityType() {
        this(-1, -1,null);
    }
    public AbilityType(int maxCooldown) {
        this(maxCooldown, -1,null);
    }
    public AbilityType(int maxCooldown, int duration) {
        this(maxCooldown, duration, null);
    }
    public AbilityType(ResourceLocation texture) {
        this(-1, -1, texture);
    }
    public AbilityType(int maxCooldown, int duration, ResourceLocation texture) {
        this.maxCooldown = maxCooldown;
        this.duration = duration;
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

    /**
     * Whether or not this ability should be hidden from the player's ability menu. Defaults to false
     */
    public boolean isHidden() {
        return false;
    }
    public int getMaxCooldown() {
        return maxCooldown;
    }

    public int getDuration() {
        return duration;
    }

    public ResourceLocation getTexture() {
//        if(itemToRender == null) return null;
        return texture;
    }
}
