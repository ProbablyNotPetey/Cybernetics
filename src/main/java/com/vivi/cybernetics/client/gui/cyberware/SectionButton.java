package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
import com.vivi.cybernetics.common.cyberware.CyberwareSection;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;


public class SectionButton extends AbstractButton implements ITransparentWidget {

    private final CyberwareSection section;
    private final ResourceLocation texture;
    private final CyberwareScreen<?> screen;

    private int boxLeft;
    private int boxRight;
    private int boxTop;
    private int boxBottom;
    public SectionButton(CyberwareScreen<?> screen, int x, int y, CyberwareSection section) {
        super(x, y, 24, 24, Component.empty());
        this.screen = screen;
        this.section = section;
        this.texture = new ResourceLocation(section.getId().getNamespace(), "textures/gui/cyberware/section/" + section.getId().getPath() + ".png");
        alpha = 0.0f;
        visible = false;
        active = false;
    }

    public SectionButton setBox(int boxLeft, int boxRight, int boxTop, int boxBottom) {
        this.boxLeft = boxLeft;
        this.boxRight = boxRight;
        this.boxTop = boxTop;
        this.boxBottom = boxBottom;
        return this;
    }

    @Override
    public void onPress() {
        if(screen.getCurrentState() instanceof State.Main) {
            screen.setCurrentState(new State.Section(screen, section, section.getType().getXOffset(), section.getType().getYOffset()));
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    public CyberwareSection getSection() {
        return section;
    }

    @Override
    public float getTransparency() {
        return alpha;
    }

    @Override
    public void setTransparency(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        visible = alpha > 0.0f;
        active = visible;
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
        RenderSystem.setShaderColor(color, color, color, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
//            int u = this.isHoveredOrFocused() ? width : 0;

        enableScissor(boxLeft, boxTop, boxRight, boxBottom);
        blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
        disableScissor();
    }
}
