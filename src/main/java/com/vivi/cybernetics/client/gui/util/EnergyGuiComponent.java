package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EnergyGuiComponent extends CybAbstractWidget {

    private int energyStored;
    private int maxEnergy;
    public EnergyGuiComponent(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.playSound = false;
    }

    public List<Component> getTooltip(int energyStored, int maxEnergy) {
        return List.of(Component.literal(formatInt(energyStored) + "/" + formatInt(maxEnergy) + " FE"));
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

    public void setEnergyStored(int energyStored) {
        this.energyStored = energyStored;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int scaledHeight = (int) (height * (energyStored / (double) maxEnergy));
        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
        guiGraphics.fillGradient(getX(), getY() + (height - scaledHeight), getX() + width, getY() + height, 0xfffc3232, 0xff780e00); // color is ARGB

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
