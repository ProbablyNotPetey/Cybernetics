package com.vivi.cybernetics.client.gui.cyberware;


import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        //isHovered() instead of isHoveredOrFocused(), because idrk how focusing works lmao this is a bad hack
        float color = this.isHovered() ? 1.0F : 0.65F;
        guiGraphics.setColor(color, color, color, alpha);
        guiGraphics.blit(TEXTURE, getX(), getY(), 60, 0, 23, this.width, this.height, 32, 32);
        RenderHelper.resetShaderColor(guiGraphics);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
