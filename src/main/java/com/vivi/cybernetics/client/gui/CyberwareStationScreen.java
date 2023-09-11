package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.menu.CyberwareStationMenu;
import com.vivi.cybernetics.util.MouseHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, startX, startY, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(poseStack, startX, startY);

        energyGuiComponent.draw(poseStack, menu.getStoredEnergy(), menu.getMaxEnergy());
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY - 2, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX + 10, (float)this.inventoryLabelY + 9, 4210752);
        if(MouseHelper.isHovering(energyGuiComponent.x, energyGuiComponent.y, energyGuiComponent.width, energyGuiComponent.height, mouseX, mouseY)) {
            renderTooltip(poseStack, energyGuiComponent.getTooltip(menu.getStoredEnergy(), menu.getMaxEnergy()), Optional.empty(), mouseX - startX, mouseY - startY);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    private void renderProgressArrow(PoseStack poseStack, int xOffset, int yOffset) {
        if(menu.isCrafting()) {
            this.blit(poseStack, xOffset + 31, yOffset + 58, 0, 176, menu.getScaledProgress(), 14);
        }
    }
}
