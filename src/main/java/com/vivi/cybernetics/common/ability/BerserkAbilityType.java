package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.shader.BerserkRenderer;
import com.vivi.cybernetics.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class BerserkAbilityType extends AbilityType {

    protected final int duration;
    protected final double damageBoost;
    protected final int regenAmp;
    protected final double speed;
    private static final UUID speedUUID = UUID.fromString("346cc9b7-3c4a-4bee-9c8a-360daa1381ea");
    private static final UUID attackSpeedUUID = UUID.fromString("e12b8786-1e2f-4b72-b91a-fec38b02f905");
    private static final UUID fistAttackUUID = UUID.fromString("2415d2cf-bf8a-42e4-a62b-bd0eca1612aa");
    public BerserkAbilityType(ResourceLocation texture, int duration, int maxCooldown, double damageBoost, int regenAmp, double speed) {
        super(maxCooldown, texture);
        this.duration = duration;
        this.damageBoost = damageBoost;
        this.regenAmp = regenAmp;
        this.speed = speed;
    }
    public BerserkAbilityType(ResourceLocation texture, int duration, int maxCooldown) {
        this(texture, duration, maxCooldown, 0.0, 0, 0.3);
    }


    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        if(level.isClientSide) {
            //enable orange post shader
            BerserkRenderer.getInstance().start(20, Easing.EXPO_OUT);
//            Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation(Cybernetics.MOD_ID, "shaders/post/tint.json"));
        }
    }

    @Override
    public void onDisable(Ability ability, Level level, Player player) {
        super.onDisable(ability, level, player);
        //remove speed boost attribute
        if(level.isClientSide) {
            //disable orange post shader
            BerserkRenderer.getInstance().stop(30, Easing.EXPO_IN_OUT);
//            Minecraft.getInstance().gameRenderer.shutdownEffect();
            return;
        }
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if(speed != null) {
            speed.removeModifier(speedUUID);
        }

        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        if(attackSpeed != null) {
            attackSpeed.removeModifier(attackSpeedUUID);
        }

        AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if(attackDamage != null) {
            attackDamage.removeModifier(fistAttackUUID);
        }

        if(player.hasEffect(MobEffects.DAMAGE_RESISTANCE) && player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 3) {
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        }
        if(player.hasEffect(MobEffects.REGENERATION) && player.getEffect(MobEffects.REGENERATION).getAmplifier() == regenAmp) {
            player.removeEffect(MobEffects.REGENERATION);
        }
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(!ability.isEnabled()) return;
        if(!level.isClientSide) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if(attackDamage != null && damageBoost > 0) {
                if(player.getMainHandItem().isEmpty()) {
                    if(attackDamage.getModifier(fistAttackUUID) == null) {
                        attackDamage.addPermanentModifier(new AttributeModifier(fistAttackUUID, "Berserk Attack Damage", damageBoost, AttributeModifier.Operation.ADDITION));
                    }
                }
                else {
                    attackDamage.removeModifier(fistAttackUUID);
                }
            }

            AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if(speed != null && speed.getModifier(speedUUID) == null) {
                speed.addPermanentModifier(new AttributeModifier(speedUUID, "Berserk Speed", this.speed, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }

            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if(attackSpeed != null && attackSpeed.getModifier(attackSpeedUUID) == null) {
                attackSpeed.addPermanentModifier(new AttributeModifier(attackSpeedUUID, "Berserk Attack Speed", 0.7, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }

            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 319, 3, false, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 319, regenAmp, false, true, true));
        }

        if(ability.getElapsedTime() >= duration) {
            ability.disable(player);
        }
    }
}
