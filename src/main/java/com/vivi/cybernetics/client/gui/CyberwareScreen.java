package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.network.PacketHandler;
import com.vivi.cybernetics.network.packet.C2SSwitchActiveSlotPacket;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.util.MouseHelper;
import net.minecraft.ChatFormatting;
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

import java.util.List;
import java.util.Optional;

public class CyberwareScreen<T extends CyberwareMenu> extends AbstractContainerScreen<T> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/player_cyberware.png");
    private final NonNullList<CyberwareButton> buttons = NonNullList.create();

    public CyberwareScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 256;
        this.inventoryLabelY = this.imageHeight - 60;
    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        try {
            for (int i = 0; i < menu.getCyberware().getSections().size(); i++) {
                int j = i % 3;
                int k = i / 3;
                addCyberwareButton(new SectionButton(menu.getCyberware().getSections().get(i).getType(), leftPos + 10 + j * 25, topPos + 10 + k * 25, 20));
            }
        } catch (Exception e) {
            Cybernetics.LOGGER.error("Could not initialize cyberware screen", e);
        }
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
        renderTooltip(pPoseStack, pMouseX, pMouseY);
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

    private void addCyberwareButton(CyberwareButton button) {
        addRenderableWidget(button);
        buttons.add(button);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
//        if(MouseHelper.isHovering(energyGuiComponent.x, energyGuiComponent.y, energyGuiComponent.width, energyGuiComponent.height, mouseX, mouseY)) {
//            renderTooltip(poseStack, energyGuiComponent.getTooltip(menu.getStoredEnergy(), menu.getMaxEnergy()), Optional.empty(), mouseX - startX, mouseY - startY);
//        }
        buttons.forEach(button -> {
            List<Component> tooltip = button.getTooltip();
            if(tooltip != null) {
                if(MouseHelper.isHovering(button.x, button.y, button.getWidth(), button.getHeight(), mouseX, mouseY)) {
                    renderTooltip(poseStack, tooltip, Optional.empty(), mouseX - leftPos, mouseY - topPos);
                }
            }
        });

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
            RenderSystem.setShaderTexture(0, CyberwareScreen.TEXTURE);
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
    class TextButton extends CyberwareButton {

        public TextButton(int pX, int pY, int pWidth, Component pMessage) {
            super(pX, pY, pWidth, pMessage);
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

        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
    class SectionButton extends CyberwareButton {

        private CyberwareSectionType section;
        public SectionButton(CyberwareSectionType section, int pX, int pY, int pWidth) {
            super(pX, pY, pWidth);
            this.section = section;
        }

        @Override
        public void onPress() {
            if(!selected) {
                CyberwareScreen.this.updateButtons(this);
                PacketHandler.sendToServer(new C2SSwitchActiveSlotPacket(section));
                CyberwareScreen.this.menu.switchActiveSlots(section);

            }
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        public List<Component> getTooltip() {
            ResourceLocation id = ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getKey(section);
            return List.of(Component.translatable("tooltip." + id.getNamespace() + ".section." + id.getPath()).withStyle(ChatFormatting.RED));
        }


        @Override
        void update() {

        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, section.getTexture());
            this.blit(pPoseStack, this.x + 2, this.y + 2, section.getTextureX(), section.getTextureY(), 16, 16, section.getTextureWidth(), section.getTextureHeight());
        }
    }
}
