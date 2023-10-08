package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.IScalableWidget;
import com.vivi.cybernetics.client.gui.util.CybAbstractContainerScreen;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.menu.CyberwareMenu;
import com.vivi.cybernetics.registry.CybCyberware;
import com.vivi.cybernetics.util.Easing;
import com.vivi.cybernetics.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

import java.util.*;

public class CyberwareScreen<T extends CyberwareMenu> extends CybAbstractContainerScreen<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/player_cyberware.png");

    private long time;
    private LocalPlayer fakePlayer;
    private EntityWidget entityWidget;
    private List<SectionButton> sectionButtons = new ArrayList<>();
    public CyberwareScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 240;
        this.imageWidth = 208;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    private int boxLeft;
    private int boxTop;
    private int boxRight;
    private int boxBottom;

    private Iterator<SectionButton> sectionButtonIterator;

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new MyButton(10, 10, 20));
        time = 0L;
        LocalPlayer player = Minecraft.getInstance().player;
        fakePlayer = new LocalPlayer(Minecraft.getInstance(), Minecraft.getInstance().level, player.connection, player.getStats(), player.getRecipeBook(), false, false);
        boxLeft = leftPos + 8;
        boxTop = topPos + 8;
        boxRight = leftPos + 200;
        boxBottom = topPos + 158;
        entityWidget = new EntityWidget(leftPos + 73, topPos - 136, 60, fakePlayer);
        addRenderableWidget(entityWidget);

        moveWidget(entityWidget, leftPos + 73, topPos + 16, 30, Easing.QUART_OUT);

        CybCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().forEach(type -> {
            SectionButton button = new SectionButton(type);
            sectionButtons.add(button);
            addRenderableWidget(button);
        });

        sectionButtons.sort((button1, button2) -> {
            int y = button1.y - button2.y;
            if(y == 0) {
                return button1.x - button2.x;
            }
            return y;
        });

        sectionButtonIterator = sectionButtons.iterator();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        initSectionButtons();

        fakePlayer.tickCount++;
        time++;
    }

    private void initSectionButtons() {
        if(time < 25) return;
        if(time > 26 + (2L * sectionButtons.size())) return;

        if(time % 2 == 0) {
            try {
                SectionButton button = sectionButtonIterator.next();
                button.setVisible(true);
                moveWidget(button, button.x, button.y - 20, 30, Easing.CUBIC_OUT);
                alphaWidget(button, 1.0f, 30);
            }
            catch(NoSuchElementException ignored) {

            }
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float frameTimeDelta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, frameTimeDelta);
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

        RenderHelper.drawLine(new Vector3f(100, 100, 0), new Vector3f(200, 200, 0), new Vector4f(1.0f, 1.0f, 0.0f, 0.0f), 2, 0);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int pMouseX, int pMouseY) {
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    class EntityWidget extends CybAbstractWidget implements IScalableWidget {

        private float scale;
        private final Entity entity;
        public EntityWidget(int pX, int pY, float scale, Entity entity) {
            super(pX, pY, (int)scale, (int)scale*2, Component.empty());
            this.entity = entity;
            this.playSound = false;
            this.scale = scale;
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            enableScissor(CyberwareScreen.this.boxLeft, CyberwareScreen.this.boxTop, CyberwareScreen.this.boxRight, CyberwareScreen.this.boxBottom);
            RenderHelper.renderEntity(entity, pPoseStack, x + scale/2, y + scale*2, scale, 0.0f);
            //7.5f * (float)Math.cos((getGameTime() - startTime + pPartialTick) / 40.0f)
            disableScissor();
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        @Override
        public float getScale() {
            return scale;
        }

        @Override
        public void setScale(float scale) {
            this.scale = scale;
        }
    }

    abstract static class CyberwareButton extends AbstractButton {

        protected boolean selected = false;

        public CyberwareButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }

        public CyberwareButton(int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, CommonComponents.EMPTY);
        }

        public CyberwareButton(int pX, int pY, int pWidth) {
            this(pX, pY, pWidth, 20);
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

        public void setVisible(boolean visible) {
            this.visible = visible;
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

    class SectionButton extends CyberwareButton implements ITransparentWidget {

        private final CyberwareSectionType type;
        public SectionButton(CyberwareSectionType type) {
            super(leftPos + type.getX(), topPos + type.getY() + 20, 24, 24);
            this.type = type;
            alpha = 0.0f;
            visible = false;
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, type.getTexture());
            float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
            RenderSystem.setShaderColor(color, color, color, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
//            int u = this.isHoveredOrFocused() ? width : 0;

            enableScissor(CyberwareScreen.this.boxLeft, CyberwareScreen.this.boxTop, CyberwareScreen.this.boxRight, CyberwareScreen.this.boxBottom);
            blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width * 2, this.height * 2);
            disableScissor();
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

        @Override
        public float getAlpha() {
            return alpha;
        }
    }
}
