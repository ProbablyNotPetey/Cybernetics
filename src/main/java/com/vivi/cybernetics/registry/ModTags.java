package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item>
            HEAD = itemTag("cyberware_section/head"),
            EYES = itemTag("cyberware_section/eyes"),
            UPPER_ORGANS = itemTag("cyberware_section/upper_organs"),
            LOWER_ORGANS = itemTag("cyberware_section/lower_organs"),
            SKELETON = itemTag("cyberware_section/skeleton"),
            SKIN = itemTag("cyberware_section/skin"),
            HANDS = itemTag("cyberware_section/hands"),
            ARMS = itemTag("cyberware_section/arms"),
            LEGS = itemTag("cyberware_section/legs"),
            FEET = itemTag("cyberware_section/feet")
    ;


    private static TagKey<Item> itemTag(String name) {
        return ItemTags.create(new ResourceLocation(Cybernetics.MOD_ID, name));
    }
}
