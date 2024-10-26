package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.EnergyGuiComponent;
import com.vivi.cybernetics.client.util.MouseHelper;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.common.menu.CyberwareStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class CyberwareStationScreen extends AbstractContainerScreen<CyberwareStationMenu> {
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware_station.png");
    private EnergyGuiComponent energyGuiComponent;

    public CyberwareStationScreen(CyberwareStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageHeight = 174; //default is 166
    }

    private int startX;
    private int startY;


    @Override
    protected void init() {
        super.init();
        startX = (width - imageWidth) / 2;
        startY = (height - imageHeight) / 2;
        energyGuiComponent = new EnergyGuiComponent(startX + 8, startY + 15, 6, 70);
        addRenderableWidget(energyGuiComponent);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, startX, startY, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(guiGraphics, startX, startY);

        energyGuiComponent.setEnergyStored(menu.getStoredEnergy());
        energyGuiComponent.setMaxEnergy(menu.getMaxEnergy());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY - 2, 4210752, false);
        guiGraphics.drawString(font, this.playerInventoryTitle, this.inventoryLabelX + 10, this.inventoryLabelY + 9, 4210752, false);
        if(MouseHelper.isHovering(energyGuiComponent.getX(), energyGuiComponent.getY(), energyGuiComponent.getWidth(), energyGuiComponent.getHeight(), mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, energyGuiComponent.getTooltip(menu.getStoredEnergy(), menu.getMaxEnergy()), Optional.empty(), mouseX - startX, mouseY - startY);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, xOffset + 31, yOffset + 58, 0, 176, menu.getScaledProgress(), 14);
        }
    }
}
