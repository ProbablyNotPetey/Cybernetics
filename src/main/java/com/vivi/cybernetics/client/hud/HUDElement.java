package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;

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
    public void render(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        renderElement(poseStack, partialTick, screenWidth, screenHeight);
        poseStack.popPose();
    }
    public abstract void renderElement(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight);
}
