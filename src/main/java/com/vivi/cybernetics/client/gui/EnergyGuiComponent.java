package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class EnergyGuiComponent extends GuiComponent {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public EnergyGuiComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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




    public void draw(PoseStack stack, int energyStored, int maxEnergy) {
        int scaledHeight = (int) (height * (energyStored / (double) maxEnergy));
        //draws from the top, not the bottom
//        Cybernetics.LOGGER.info("Stored: " + energyStored + ", Max: " + maxEnergy);
        fillGradient(stack, x, y + (height - scaledHeight), x + width, y + height, 0xfffc3232, 0xff780e00); // color is ARGB
    }
}
