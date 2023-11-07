package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import javax.swing.text.html.parser.Entity;

public class CybTags {

    public static final TagKey<Item>
            OPTICS = itemTag("optics"),
            BERSERKS = itemTag("berserks"),
            SKELETONS = itemTag("skeletons"),
            DOUBLE_JUMP_ADDERS = itemTag("double_jump_adders"),


            ANY_SECTION = itemTag("cyberware_section/any"),
            HEAD_SECTION = itemTag("cyberware_section/head"),
            EYES_SECTION = itemTag("cyberware_section/eyes"),
            UPPER_ORGANS_SECTION = itemTag("cyberware_section/upper_organs"),
            LOWER_ORGANS_SECTION = itemTag("cyberware_section/lower_organs"),
            SKELETON_SECTION = itemTag("cyberware_section/skeleton"),
            SKIN_SECTION = itemTag("cyberware_section/skin"),
            HANDS_SECTION = itemTag("cyberware_section/hands"),
            ARMS_SECTION = itemTag("cyberware_section/arms"),
            LEGS_SECTION = itemTag("cyberware_section/legs"),
            FEET_SECTION = itemTag("cyberware_section/feet")
    ;

    public static final TagKey<EntityType<?>>
            PROJECTILES_ALWAYS_HIT = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Cybernetics.MOD_ID, "projectiles_always_hit"));


    private static TagKey<Item> itemTag(String name) {
        return ItemTags.create(new ResourceLocation(Cybernetics.MOD_ID, name));
    }
}
