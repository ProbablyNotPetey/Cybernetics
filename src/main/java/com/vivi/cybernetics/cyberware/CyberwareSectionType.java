package com.vivi.cybernetics.cyberware;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CyberwareSectionType {

    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/section_buttons.png");

    private final TagKey<Item> tag;
    private final int size;
    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;

    public CyberwareSectionType(TagKey<Item> tag, int size, ResourceLocation texture, int textureX, int textureY) {
        this.tag = tag;
        this.size = size;
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
    }
    public CyberwareSectionType(TagKey<Item> tag, int size, int textureX, int textureY) {
        this(tag, size, DEFAULT_TEXTURE, textureX, textureY);
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

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getTextureX() {
        return textureX;
    }

    public int getTextureY() {
        return textureY;
    }
}
