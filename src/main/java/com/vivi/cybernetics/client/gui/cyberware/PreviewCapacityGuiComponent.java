package com.vivi.cybernetics.client.gui.cyberware;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class PreviewCapacityGuiComponent extends CapacityGuiComponent {

    private float time;
    private long startTime;
    private static final float HALF_PERIOD = 15;
    private int coefficient;
    private int previewCapacity = -1;
    public PreviewCapacityGuiComponent(int x, int y) {
        super(x, y);
        startTime = Minecraft.getInstance().level.getGameTime();
    }

    public void resetStartTime() {
        startTime = Minecraft.getInstance().level.getGameTime();
    }

    public void setRemove(boolean remove) {
        if(remove) coefficient = 1;
        else coefficient = -1;
    }

    public void setPreviewCapacity(int previewCapacity) {
        this.previewCapacity = previewCapacity;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if(previewCapacity == -1) return;

        time = (Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getPartialTick()) - startTime;

        int scaledOldHeight = (int) (height * (1.0 * capacity / maxCapacity));
        int scaledNewHeight = (int) (height * (1.0 * previewCapacity / maxCapacity));
        if(scaledOldHeight > height) scaledOldHeight = height;
        if(scaledNewHeight > height) scaledNewHeight = height;

        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
//        fillGradient(poseStack, x, y + (height - scaledHeight), x + width, y + height, 0xfffc3232, 0xff780e00); // color is ARGB


        float alpha = 0.5f * (coefficient * Mth.cos((Mth.PI / HALF_PERIOD) * time) + 1);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        int u = previewCapacity > maxCapacity ? 24 : 0;
        guiGraphics.blit(TEXTURE, getX(), getY() + (height - scaledNewHeight), u, height - scaledNewHeight, width, scaledNewHeight - scaledOldHeight, 144, 144);

    }
}
