package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CyberwareScreen<T extends CyberwareMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/player_cyberware.png");

    private final List<WidgetMovement> widgetsToMove = new ArrayList<>();

    public CyberwareScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 60;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new MyButton(10, 10, 20));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float frameTimeDelta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, frameTimeDelta);
        renderTooltip(pPoseStack, pMouseX, pMouseY);

        //on the client so this is fine
        long currentGameTime = Minecraft.getInstance().player.level.getGameTime();
        float partialTick = Minecraft.getInstance().getPartialTick();
        for(int i = 0; i < widgetsToMove.size(); i++) {
            WidgetMovement movement = widgetsToMove.get(i);
            movement.update(currentGameTime, partialTick);
            if(movement.isDone()) {
                widgetsToMove.remove(i);
                i--;
            }
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < menu.slots.size(); i++) {
            if(i == menu.slots.size() - 36) break; //inventory slots should be the last 36 slots in the menu
            if(!menu.getSlot(i).isActive()) continue;
            int v = menu.getSlot(i).hasItem() ? 18 : 0;
            this.blit(pPoseStack, leftPos + menu.getSlot(i).x - 1, topPos + menu.getSlot(i).y - 1, 176, v, 18, 18);
        }

//        InventoryScreen.renderEntityInInventory(leftPos + 130, topPos + 123, 50, (float)(leftPos + 130) - pMouseX, (float)(topPos + 40) - pMouseY, Minecraft.getInstance().player);

    }

    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration) {
        long currentGameTime = Minecraft.getInstance().player.level.getGameTime();
        widgetsToMove.add(new WidgetMovement(widget, newX, newY, currentGameTime, duration));
    }
    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration, Easing easing) {
        long currentGameTime = Minecraft.getInstance().player.level.getGameTime();
        widgetsToMove.add(new WidgetMovement(widget, newX, newY, currentGameTime, duration, easing));
    }


    abstract static class CyberwareButton extends AbstractButton {

        protected boolean selected = false;

        public CyberwareButton(int pX, int pY, int pWidth, Component pMessage) {
            super(pX, pY, pWidth, 20, pMessage);
        }

        public CyberwareButton(int pX, int pY, int pWidth) {
            super(pX, pY, pWidth, 20, CommonComponents.EMPTY);
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, CyberwareScreenOld.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            Cybernetics.LOGGER.info("Active? " + this.active + ", Selected? " + this.selected + ", isHoveredOrFocused? " + this.isHoveredOrFocused());
            int u = 176;
            int v = 48;
            if (this.selected) {
                v += this.height * 2;
            } else if (this.isHoveredOrFocused()) {
                v += this.height;
            }
            this.blit(pPoseStack, this.x, this.y, u, v, this.width / 2, this.height);
            this.blit(pPoseStack, this.x + this.width / 2, this.y, u + 80 - this.width / 2, v, this.width / 2, this.height);
        }

        abstract void update();

        abstract List<Component> getTooltip();

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    class MyButton extends CyberwareButton {

        public MyButton(int pX, int pY, int pWidth) {
            super(pX, pY, pWidth);
        }

        @Override
        void update() {

        }

        @Override
        List<Component> getTooltip() {
            return null;
        }

        @Override
        public void onPress() {
            CyberwareScreen.this.moveWidget(this, 120, 120, 40, Easing.BACK_IN_OUT);
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
}
