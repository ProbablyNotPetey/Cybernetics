package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class CapacityGuiComponent extends CybAbstractWidget {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/bar_filled.png");

    protected int capacity;
    protected int maxCapacity;
    public CapacityGuiComponent(int x, int y) {
        super(x, y, 23, 140, Component.empty());
    }

    public List<Component> getTooltip(int capacity, int maxCapacity) {
        ChatFormatting color = (capacity > maxCapacity) ? ChatFormatting.DARK_RED : ChatFormatting.GOLD;
        return List.of(Component.translatable("tooltip.cybernetics.capacity").append(": ").append(Component.literal(capacity + "/" + maxCapacity)).withStyle(color));
    }

    //code somewhat based on the one probe code
    private String formatInt(int num) {

        if(num < 1000) return String.valueOf(num);

        //number of 0s / 3 (1000 has 3 0s)
        int prefixId = (int) (Math.log10(num) / 3.0);
        char prefix = "kMG".charAt(prefixId - 1);

        //divide by 1000^prefixId to get formatted number
        return String.format("%.1f%s", (double) (num / Math.pow(1000, prefixId)), prefix);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int scaledHeight = (int) (height * (capacity / (double) maxCapacity));
        if(scaledHeight > height) scaledHeight = height;
        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
//        fillGradient(poseStack, x, y + (height - scaledHeight), x + width, y + height, 0xfffc3232, 0xff780e00); // color is ARGB
        RenderSystem.enableBlend();
        int u = capacity > maxCapacity ? 24 : 0;
        guiGraphics.blit(TEXTURE, getX(), getY() + (height - scaledHeight), u, height - scaledHeight, width, scaledHeight, 144, 144);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
