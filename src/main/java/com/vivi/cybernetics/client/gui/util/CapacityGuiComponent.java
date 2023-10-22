package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class CapacityGuiComponent extends GuiComponent {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    private final boolean left;
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/bar_filled.png");

    public CapacityGuiComponent(int x, int y, boolean left) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 82;
        this.left = left;
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




    public void draw(PoseStack poseStack, int capacity, int maxCapacity) {
        int scaledHeight = (int) (height * (capacity / (double) maxCapacity));
        if(scaledHeight > height) scaledHeight = height;
        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
//        fillGradient(poseStack, x, y + (height - scaledHeight), x + width, y + height, 0xfffc3232, 0xff780e00); // color is ARGB
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int u = left ? 0 : 16;
        if(capacity > maxCapacity) u += 32;
        blit(poseStack, x, y + (height - scaledHeight), u, height - scaledHeight, width, scaledHeight, 96, 96);
    }
}
