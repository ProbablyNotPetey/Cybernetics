package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class MaskWidget extends CybAbstractWidget implements ITransparentWidget {
    public MaskWidget(int pX, int pY) {
        super(pX, pY, 22, 18, Component.empty());
        this.playSound = false;
        this.alpha = 0.0f;
        this.visible = false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.visible = (alpha > 0.0f);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        guiGraphics.blit(CyberwareScreen.TEXTURE, getX(), getY(), 300, 27, 9, width, height, 226, 154);
    }

    @Override
    public float getTransparency() {
        return alpha;
    }

    @Override
    public void setTransparency(float alpha) {
        this.alpha = alpha;
    }
}
