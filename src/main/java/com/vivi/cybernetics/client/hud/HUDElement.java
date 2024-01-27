package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public abstract class HUDElement implements IHUDElement {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    public HUDElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        renderElement(guiGraphics, partialTick, screenWidth, screenHeight);
        guiGraphics.pose().popPose();
    }
    public abstract void renderElement(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight);
}
