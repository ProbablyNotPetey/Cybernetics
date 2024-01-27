package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public class TextWidget extends CybAbstractWidget {

    private final Screen screen;
    private Component text;
    private MutableComponent mutableText;
    private boolean drawUnderscore = false;
    private int animationMode = 0; //0: no animate, 1: delete, 2: write
    private int character = 0;
    private Font font;
    private int maxWidth;

    public TextWidget(Screen screen, int pX, int pY) {
        this(screen, pX, pY, -1);
    }

    public TextWidget(Screen screen, int x, int y, int maxWidth) {
        super(x, y, 1, 1, Component.empty());
        this.screen = screen;
        this.playSound = false;
        text = Component.empty();
        mutableText = text.copy();
        this.font = Minecraft.getInstance().font;
        this.maxWidth = maxWidth;
    }


    public void tick(long time) {
        if (time % 10 == 0) {
            drawUnderscore = !drawUnderscore;
        }

        if (mutableText.getString().equals("") && animationMode == 1) {
            animationMode = 2;
        }

        if (mutableText.getString().equals(text.getString()) && animationMode == 2) {
            animationMode = 0;
            character = 0;
        }


        if (animationMode == 1) {
            mutableText = Component.literal(mutableText.getString().substring(0, mutableText.getString().length() - 1));
        }
        if (animationMode == 2) {
            mutableText = Component.literal(text.getString(character++));
        }


    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        MutableComponent drawText = drawUnderscore ? mutableText.copy().append("_") : mutableText.copy();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        if(maxWidth != -1) {
            int yOffset = 0;
            for(FormattedCharSequence fcq : font.split(drawText, maxWidth)) {
                guiGraphics.drawString(font, fcq, getX(), getY() + yOffset, 0xff00fff7);
                yOffset += 9;
            }
        }
        else {
            guiGraphics.drawString(font, drawText, getX(), getY(), 0xff00fff7);
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    public void setText(Component text) {
        this.text = text;
        animationMode = 1;
        character = 0;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setText(Component text, boolean instant) {
        if(instant) {
            this.text = text;
            this.mutableText = text.copy();
        }
        else {
            setText(text);
        }
    }

    public int getTextWidth() {
        return font.width(mutableText);
    }
    public int getTextHeight() {
        return font.lineHeight;
    }
}
