package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.menu.CyberwareMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class PlayerCyberwareScreen<T extends CyberwareMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/player_cyberware.png");

    private final NonNullList<CyberwareButton> buttons = NonNullList.create();

    public PlayerCyberwareScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 60;
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        try {
            for (int i = 0; i < menu.getSections().size(); i++) {
                int j = i % 3;
                int k = i / 3;
                addCyberwareButton(new SectionButton(menu.getSections().get(i).id, leftPos + 10 + j * 25, topPos + 10 + k * 25, 20));
            }
        } catch (Exception e) {
            Cybernetics.LOGGER.error("Could not load cyberware screen", e);
        }

//        addCyberwareButton(new SectionButton("head", leftPos + 10, topPos + 10, 20));
//        addCyberwareButton(new SectionButton("eyes", leftPos + 30, topPos + 10, 20));
//        addCyberwareButton(new SectionButton("upper_organs", leftPos + 50, topPos + 10, 20));
//        addCyberwareButton(new SectionButton("lower_organs", leftPos + 10, topPos + 30, 20));
//        addCyberwareButton(new SectionButton("skeleton", leftPos + 30, topPos + 30, 20));
//        addCyberwareButton(new SectionButton("skin", leftPos + 50, topPos + 30, 20));
//        addCyberwareButton(new SectionButton("hands", leftPos + 10, topPos + 50, 20));
//        addCyberwareButton(new SectionButton("arms", leftPos + 30, topPos + 50, 20));
//        addCyberwareButton(new SectionButton("legs", leftPos + 50, topPos + 50, 20));
//        addCyberwareButton(new SectionButton("feet", leftPos + 10, topPos + 70, 20));
    }

    private void addCyberwareButton(CyberwareButton button) {
        addRenderableWidget(button);
        buttons.add(button);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons(null);
    }

    public void updateButtons(CyberwareButton activeButton) {
        buttons.forEach(button -> {
//            Cybernetics.LOGGER.info("" + button.selected);
            if(activeButton != null) {
                button.setSelected(button == activeButton);
            }
            button.update();
        });
//        Cybernetics.LOGGER.info("Buttons updated");
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
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

        InventoryScreen.renderEntityInInventory(leftPos + 130, topPos + 123, 50, (float)(leftPos + 130) - pMouseX, (float)(topPos + 40) - pMouseY, Minecraft.getInstance().player);



    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
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
            RenderSystem.setShaderTexture(0, PlayerCyberwareScreen.TEXTURE);
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

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
    class TextButton extends CyberwareButton {

        public TextButton(int pX, int pY, int pWidth, Component pMessage) {
            super(pX, pY, pWidth, pMessage);
        }

        @Override
        void update() {

        }

        @Override
        public void onPress() {

        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
    class SectionButton extends CyberwareButton {

        private ResourceLocation section;
        public SectionButton(ResourceLocation section, int pX, int pY, int pWidth) {
            super(pX, pY, pWidth);
            this.section = section;
        }

        @Override
        public void onPress() {
            if(!selected) {
                Cybernetics.LOGGER.info("Pressed");
                PlayerCyberwareScreen.this.updateButtons(this);
                PlayerCyberwareScreen.this.menu.switchSlots(section);
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        @Override
        void update() {

        }
    }
}
