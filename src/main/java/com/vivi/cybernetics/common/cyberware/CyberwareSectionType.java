package com.vivi.cybernetics.common.cyberware;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CyberwareSectionType {

//    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/section_buttons.png");

    private final TagKey<Item> tag;
    private final int size;
//    private final ResourceLocation texture;
//    private final int textureX;
//    private final int textureY;
    private final int x;
    private final int y;
    private final int xOffset;
    private final int yOffset;

//    private final int textureWidth;
//    private final int textureHeight;

    public CyberwareSectionType(TagKey<Item> tag, int size, int x, int y, int yOffset) {
        this(tag, size, x, y, 100, yOffset);
    }
    public CyberwareSectionType(TagKey<Item> tag, int size, int x, int y, int xOffset, int yOffset) {
        this.tag = tag;
        this.size = size;
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof CyberwareSectionType type)) return false;
        return this.getTag().equals(type.getTag()) && this.size == type.getSize();
    }

//    public ResourceLocation getTexture() {
//        return texture;
//    }

//    public int getTextureX() {
//        return textureX;
//    }
//
//    public int getTextureY() {
//        return textureY;
//    }
//
//    public int getTextureWidth() {
//        return textureWidth;
//    }
//
//    public int getTextureHeight() {
//        return textureHeight;
//    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
