package com.vivi.cybernetics.cyberware;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CyberwareSectionType {

    private final TagKey<Item> tag;
    private final int size;
    public CyberwareSectionType(TagKey<Item> tag, int size) {
        this.tag = tag;
        this.size = size;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CyberwareSectionType type)) return false;
        return this.getTag().equals(type.getTag()) && this.size == type.getSize();
    }
}
