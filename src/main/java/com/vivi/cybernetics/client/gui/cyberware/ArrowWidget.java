package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ArrowWidget extends CybAbstractWidget implements ITransparentWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/arrows.png");

    private Mode mode;
    public ArrowWidget(int pX, int pY) {
        super(pX, pY, 7, 19, Component.empty());
        this.playSound = false;
        this.alpha = 0.0f;
        this.visible = false;
        this.mode = Mode.NONE;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.visible = (alpha > 0.0f);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        int u = 0, v = 0;
        switch (mode) {
            case IN_OK:
                break;
            case IN_ERROR:
                u = 8;
                break;
            case IN_WARN:
                u = 16;
                break;
            case OUT_OK:
                v = 20;
                break;
            case OUT_ERROR:
                u = 8;
                v = 20;
                break;
            case NONE:
                u = 16;
                v = 20;
        }
        guiGraphics.blit(TEXTURE, getX(), getY(), u, v,  width, height, 48, 48);
        RenderHelper.resetShaderColor(guiGraphics);
    }

    @Override
    public float getTransparency() {
        return alpha;
    }

    @Override
    public void setTransparency(float alpha) {
        this.alpha = alpha;
    }

    public static enum Mode {
        IN_OK,
        IN_ERROR,
        IN_WARN,
        OUT_OK,
        OUT_ERROR,
        NONE
    }
}
