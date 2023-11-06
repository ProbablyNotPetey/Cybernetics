package com.vivi.cybernetics.client.gui.cyberware;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BackButton extends AbstractButton {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/buttons.png");

    private final CyberwareScreen<?> screen;
    public BackButton(CyberwareScreen<?> screen, int pX, int pY) {
        super(pX, pY, 9,9,Component.empty());
        this.screen = screen;
    }

    @Override
    public void onPress() {
        if(screen.getCurrentState() instanceof State.Section state && state.canGoBack()) {
            screen.setCurrentState(new State.Main(screen));
        }
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
        RenderSystem.setShaderColor(color, color, color, alpha);
        setBlitOffset(60);
        blit(pPoseStack, this.x, this.y, this.width, this.height, 0, 23, this.width, this.height, 32, 32);
        setBlitOffset(0);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
