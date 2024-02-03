package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.common.registry.CybAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class Ability implements INBTSerializable<CompoundTag> {

    private AbilityType type;
    private boolean enabled;
    private int cooldown;
    private int elapsedTime = -1;

    public Ability(AbilityType type) {
        this.type = type;
    }
    public Ability(CompoundTag tag) {
        deserializeNBT(tag);
    }

    private void onEnable(Player player) {
        elapsedTime = 0;
        this.getType().onEnable(this, player.level(), player);
    }

    private void onDisable(Player player) {
        elapsedTime = -1;
        this.getType().onDisable(this, player.level(), player);
    }

    public void tick(Player player) {
        if(cooldown > -1) cooldown--;
        this.getType().tick(this, player.level(), player);
        if(elapsedTime != -1 && enabled) elapsedTime++;
    }

    public boolean enable(Player player) {
        if(cooldown > -1) return false;
        this.enabled = true;
        onEnable(player);
        return true;
    }

    /**
     * Note: do NOT disable an ability on the same tick it was enabled! This will cause sync issues for abilities that need to run onEnabled on both sides.
     * <p>
     * todo: prevent this from happening.
     */
    public boolean disable(Player player) {
        this.enabled = false;
        onDisable(player);
        return true;
    }


    public AbilityType getType() {
        return type;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ResourceLocation id = CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(type);
        tag.putString("id", id.toString());
        tag.putBoolean("enabled", enabled);
        tag.putInt("cooldown", cooldown);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
        type = CybAbilities.ABILITY_TYPE_REGISTRY.get().getValue(id);
        enabled = tag.getBoolean("enabled");
        cooldown = tag.getInt("cooldown");
    }
}
