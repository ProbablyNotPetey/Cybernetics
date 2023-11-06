package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
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
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.visible = (alpha > 0.0f);
        this.setBlitOffset(300);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.setBlitOffset(0);

    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CyberwareScreen.TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        this.blit(pPoseStack, x, y, 27, 9,  width, height);
        RenderSystem.disableBlend();
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
