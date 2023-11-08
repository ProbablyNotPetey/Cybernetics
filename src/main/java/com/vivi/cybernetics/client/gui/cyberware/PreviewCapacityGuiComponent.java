package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

public class PreviewCapacityGuiComponent extends CapacityGuiComponent {

    private float time;
    private long startTime;
    private static final float HALF_PERIOD = 15;
    private int coefficient;
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

    public void draw(PoseStack poseStack, int oldCapacity, int newCapacity, int maxCapacity) {
        time = (Minecraft.getInstance().level.getGameTime() + Minecraft.getInstance().getPartialTick()) - startTime;

        int scaledOldHeight = (int) (height * (1.0 * oldCapacity / maxCapacity));
        int scaledNewHeight = (int) (height * (1.0 * newCapacity / maxCapacity));
        if(scaledOldHeight > height) scaledOldHeight = height;
        if(scaledNewHeight > height) scaledNewHeight = height;

        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
//        fillGradient(poseStack, x, y + (height - scaledHeight), x + width, y + height, 0xfffc3232, 0xff780e00); // color is ARGB

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        float alpha = 0.5f * (coefficient * Mth.cos((Mth.PI / HALF_PERIOD) * time) + 1);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int u = newCapacity > maxCapacity ? 24 : 0;
        blit(poseStack, x, y + (height - scaledNewHeight), u, height - scaledNewHeight, width, scaledNewHeight - scaledOldHeight, 144, 144);

        RenderSystem.disableBlend();
    }
}
