package com.vivi.cybernetics.common.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AttributeCyberwareItem extends CyberwareItem {

    private final Pair<Attribute, AttributeModifier>[] attributes;

    @SafeVarargs
    public AttributeCyberwareItem(Properties pProperties, Pair<Attribute, AttributeModifier>... attributes) {
        super(pProperties);
        this.attributes = attributes;
    }

    @Override
    public void cyberwareTick(ItemStack stack, Level level, Player player) {
        super.cyberwareTick(stack, level, player);
        if(level.isClientSide) return;
        for(int i = 0; i < attributes.length; i++) {
            Pair<Attribute, AttributeModifier> attribute = attributes[i];
            AttributeInstance playerAttribute = player.getAttribute(attribute.getFirst());
            //add if player dead as well
            if(playerAttribute != null && !playerAttribute.hasModifier(attribute.getSecond())) {
                playerAttribute.addPermanentModifier(attribute.getSecond());
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, Level level, Player player) {
        super.onUnequip(stack, level, player);

        for(int i = 0; i < attributes.length; i++) {
            Pair<Attribute, AttributeModifier> attribute = attributes[i];
            AttributeInstance playerAttribute = player.getAttribute(attribute.getFirst());
            if(playerAttribute != null && playerAttribute.hasModifier(attribute.getSecond())) {
                playerAttribute.removeModifier(attribute.getSecond());
            }
        }
    }

    public Pair<Attribute, AttributeModifier>[] getAttributes() {
        return attributes;
    }
}
