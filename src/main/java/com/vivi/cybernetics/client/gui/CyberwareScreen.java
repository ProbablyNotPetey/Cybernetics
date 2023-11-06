package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.event.CybGuiEventListener;
import com.vivi.cybernetics.client.gui.event.GuiEvent;
import com.vivi.cybernetics.client.gui.event.StateEvent;
import com.vivi.cybernetics.client.gui.util.*;
import com.vivi.cybernetics.common.cyberware.CyberwareSection;
import com.vivi.cybernetics.common.menu.CyberwareMenu;
import com.vivi.cybernetics.common.menu.InventorySlot;
import com.vivi.cybernetics.common.menu.deprecated.CyberwareMenuOld;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.C2SSwitchActiveSlotPacket;
import com.vivi.cybernetics.client.util.Easing;
import com.vivi.cybernetics.client.util.MouseHelper;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.client.util.ScreenHelper;
import com.vivi.cybernetics.server.network.packet.C2SSwitchPagePacket;
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
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/background.png");

    public static final ResourceLocation SLOT_TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/slots.png");

    protected LocalPlayer fakePlayer;
    protected EntityWidget entityWidget;
    protected List<SectionButton> sectionButtons = new ArrayList<>();
    protected TextWidget textWidget;
    protected State state;
    protected List<MaskWidget> itemMasks = new ArrayList<>();
    protected CapacityGuiComponent capacityComponent;
    protected int currentPage;

    protected List<EntityWidgetRotate> entityWidgetsToRotate = new ArrayList<>();
    public CyberwareScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 240;
        this.imageWidth = 154;
        this.inventoryLabelY = this.imageHeight - 94;
    }


    protected int boxLeft;
    protected int boxTop;
    protected int boxRight;
    protected int boxBottom;

    protected Iterator<SectionButton> sectionButtonIterator;
    protected Iterator<MaskWidget> maskWidgetIterator;

    @Override
    protected void init() {
        super.init();
        entityWidgetsToRotate.clear();
        itemMasks.clear();
        LocalPlayer player = Minecraft.getInstance().player;
        fakePlayer = new LocalPlayer(Minecraft.getInstance(), Minecraft.getInstance().level, player.connection, player.getStats(), player.getRecipeBook(), false, false);
        boxLeft = leftPos + 5;
        boxTop = topPos + 5;
        boxRight = leftPos + 221;
        boxBottom = topPos + 149;
        state = State.MAIN;
        currentPage = -1;

        menu.getCyberware().getSections().forEach(section -> {
            SectionButton button = new SectionButton(section.getId(), section);
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
        scheduleTask(15, 16 + (2 * sectionButtons.size()), this::initSectionButtons);

        addRenderableWidget(new BackButton(leftPos + 199, topPos + 9));

        entityWidget = new EntityWidget(leftPos + 91, topPos - 136, 60, fakePlayer);
        addRenderableWidget(entityWidget);
        moveWidget(entityWidget, leftPos + 91, topPos + 16, 20, Easing.QUART_OUT);

        textWidget = new TextWidget(this, leftPos + 28, topPos + 9);
        addRenderableWidget(textWidget);
        textWidget.setText(Component.translatable("tooltip.cybernetics.section"));

        int slotX = 36, slotY = 32;
        int rows = 4;

        for(int i = 0; i < menu.getCyberware().getLongestSectionSize(); i++) {
//            addSlot(new CyberwareSlot(cyberware, i, slotX + ((counter % 3) * 24) - 1, slotY + ((counter / 3) * 21) + 1, this.inventory.player));
            itemMasks.add(new MaskWidget(leftPos + slotX + ((i % rows) * 25) - 4, topPos + slotY + ((i / rows) * 21)));
        }
        slotY = 84;
        for(int i = 0; i < 12; i++) {
            itemMasks.add(new MaskWidget(leftPos + slotX + ((i % rows) * 25) - 4, topPos + slotY + ((i / rows) * 21)));
        }
        maskWidgetIterator = itemMasks.iterator();

        capacityComponent = new CapacityGuiComponent(leftPos + 7, topPos + 7);
//        Cybernetics.LOGGER.info("Capacity: " + menu.getStoredCapacity());
//        Cybernetics.LOGGER.info("Max Capacity: " + menu.getMaxCapacity());
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        fakePlayer.tickCount++;
        textWidget.tick(time);


        this.renderables.forEach(widget -> {
            if(widget instanceof CyberwareButton button) {
                button.update();
            }
        });
    }

    private void initSectionButtons() {

        if(time % 2 == 0 && sectionButtonIterator.hasNext()) {
            SectionButton button = sectionButtonIterator.next();
//                button.setVisible(true);
            moveWidget(button, button.x, button.y - 20, 15, Easing.CUBIC_OUT);
            alphaWidget(button, 1.0f, 15);
        }
    }


    private void showMasks() {
        itemMasks.forEach(mask -> {
            mask.setAlpha(1.0f);
        });
    }
    private void hideMasks() {
        itemMasks.forEach(mask -> {
            mask.setAlpha(0.0f);
        });
    }
    private void fadeInMasks() {
        itemMasks.forEach(widget -> {
            alphaWidget(widget, 1.0f, 5);
        });
    }
    private void fadeOutMasks() {
        if(maskWidgetIterator.hasNext()) {
            MaskWidget widget = maskWidgetIterator.next();
            alphaWidget(widget, 0.0f, 15);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float frameTimeDelta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, frameTimeDelta);

//        RenderSystem.enableDepthTest();
//        RenderSystem.applyModelViewMatrix();
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        pPoseStack.pushPose();
//        pPoseStack.translate(0, 0, 600);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
//        pPoseStack.popPose();

        itemMasks.forEach(mask -> mask.render(pPoseStack, pMouseX, pMouseY, frameTimeDelta));

//        for(int i = 0; i < entityWidgetsToRotate.size(); i++) {
//            EntityWidgetRotate rotate = entityWidgetsToRotate.get(i);
//            rotate.update(getGameTime(), getPartialTick());
//            if(rotate.isDone()) {
//                entityWidgetsToRotate.remove(i);
//                i--;
//            }
//        }
    }

    public void rotateEntity(EntityWidget widget, float rotation, int duration) {
        ScreenHelper.addAnimation(this, widget::getRotation, widget::setRotation, rotation, duration);
//        entityWidgetsToRotate.add(new EntityWidgetRotate(widget, rotation, getGameTime(), duration));
    }
    public void rotateEntity(EntityWidget widget, float rotation, int duration, Easing easing) {
        ScreenHelper.addAnimation(this, widget::getRotation, widget::setRotation, rotation, duration, easing);
//        entityWidgetsToRotate.add(new EntityWidgetRotate(widget, rotation, getGameTime(), duration, easing));
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        RenderSystem.setShaderTexture(0, SLOT_TEXTURE);

        for (int i = 0; i < menu.slots.size(); i++) {
            if(!menu.getSlot(i).isActive()) continue;
            boolean isInvSlot = menu.getSlot(i) instanceof InventorySlot;
            boolean slotEmpty = !menu.getSlot(i).hasItem();
            int u = slotEmpty && !isInvSlot ? 23 : 0;
            int v = isInvSlot ? 19 : 0;
            blit(pPoseStack, leftPos + menu.getSlot(i).x - 5, topPos + menu.getSlot(i).y - 1, u, v, 22, 18, 64, 64);
        }

        capacityComponent.draw(pPoseStack, menu.getStoredCapacity(), menu.getMaxCapacity());

    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        if(MouseHelper.isHovering(capacityComponent.x, capacityComponent.y, capacityComponent.width, capacityComponent.height, mouseX, mouseY)) {
            renderTooltip(poseStack, capacityComponent.getTooltip(menu.getStoredCapacity(), menu.getMaxCapacity()), Optional.empty(), mouseX - leftPos, mouseY - topPos);
        }
    }

    public void updateText(Component text) {
        this.textWidget.setText(text);
    }

    public void updateState(Object... data) {
        state = state.getNextState();
        this.broadcastGuiEvent(new StateEvent(state).addData(data));
    }

    public void updateState() {
        state = state.getNextState();
        this.broadcastGuiEvent(new StateEvent(state));
    }

    @Override
    public void clearAll() {
        super.clearAll();
        entityWidgetsToRotate.clear();
    }

    @Override
    public void onEvent(GuiEvent event) {
        if(event instanceof StateEvent stateEvent) {
            if(stateEvent.getState() == State.TRANSITION_SUB) {
                scheduleTask(15, this::showMasks);
                maskWidgetIterator = itemMasks.iterator();
                scheduleTask(20, 21 + (2 * itemMasks.size()), this::fadeOutMasks);
            }
            else if(stateEvent.getState() == State.TRANSITION_MAIN) {
                fadeInMasks();
                scheduleTask(6, this::hideMasks);
                sectionButtonIterator = sectionButtons.iterator();
                scheduleTask(15, 16 + (2 * sectionButtons.size()), this::initSectionButtons);
            }

            if(stateEvent.getState() == State.TRANSITION_SUB || stateEvent.getState() == State.TRANSITION_MAIN) {
                scheduleTask(20, CyberwareScreen.this::updateState);
            }
        }
    }

    public enum State {
        MAIN {
            @Override
            public State getNextState() {
                return TRANSITION_SUB;
            }

            @Override
            public String toString() {
                return "main";
            }
        },
        TRANSITION_SUB {
            @Override
            public State getNextState() {
                return SUB;
            }
            @Override
            public String toString() {
                return "transition_sub";
            }
        },
        SUB {
            @Override
            public State getNextState() {
                return TRANSITION_MAIN;
            }
            @Override
            public String toString() {
                return "sub";
            }
        },
        TRANSITION_MAIN {
            @Override
            public State getNextState() {
                return MAIN;
            }
            @Override
            public String toString() {
                return "transition_main";
            }
        };


        public abstract State getNextState();
    }


    public class EntityWidget extends CybAbstractWidget implements IScalableWidget, CybGuiEventListener {

        private float scale;
        private float rotation = 0.0f;
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
            RenderHelper.renderEntity(entity, pPoseStack, x + scale/2, y + scale*2, 20, scale, rotation);
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

        public float getRotation() {
            return rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        @Override
        public void onEvent(GuiEvent event) {
            if(event instanceof StateEvent stateEvent) {
                if(stateEvent.getState() == State.TRANSITION_SUB) {
                    List<Object> data = stateEvent.getData();
                    int x = 0, y = 0;
                    if(data.size() > 0) {
                        x = (int) data.get(0);
                        y = (int) data.get(1);
                    }
                    CyberwareScreen.this.rotateEntity(this, -45, 20, Easing.CUBIC_IN_OUT);
                    CyberwareScreen.this.scaleWidget(this, 120, 20, Easing.CUBIC_IN_OUT);
                    CyberwareScreen.this.moveWidget(this, leftPos + x, topPos + y, 20, Easing.CUBIC_IN_OUT);
                }
                else if(stateEvent.getState() == State.TRANSITION_MAIN) {
                    CyberwareScreen.this.rotateEntity(this, 0, 20, Easing.CUBIC_IN_OUT);
                    CyberwareScreen.this.scaleWidget(this, 60, 20, Easing.CUBIC_IN_OUT);
                    CyberwareScreen.this.moveWidget(this, leftPos + 91, topPos + 16, 20, Easing.CUBIC_IN_OUT);
                }
            }
        }
    }

    public class MaskWidget extends CybAbstractWidget implements ITransparentWidget {

        public MaskWidget(int pX, int pY) {
            super(pX, pY, 22, 18, Component.empty());
            this.playSound = false;
            this.alpha = 0.0f;
            this.visible = false;
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            this.visible = (alpha > 0.0f);
            this.setBlitOffset(300);
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.setBlitOffset(0);

        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, CyberwareScreen.TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
            this.blit(pPoseStack, x, y, 27, 9,  width, height);
            RenderSystem.disableBlend();
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        @Override
        public float getTransparency() {
            return alpha;
        }

        @Override
        public void setTransparency(float alpha) {
            this.alpha = alpha;
        }
    }



    abstract static class CyberwareButton extends AbstractButton implements CybGuiEventListener {

        protected boolean selected = false;
        protected ResourceLocation texture;
        protected int textureWidth = 16;
        protected int textureHeight = 16;
        public CyberwareButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }

        public CyberwareButton(int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, CommonComponents.EMPTY);
        }

        public CyberwareButton(int pX, int pY, int pWidth) {
            this(pX, pY, pWidth, 20);
        }

        public abstract void update();

        abstract List<Component> getTooltip();

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
            RenderSystem.setShaderColor(color, color, color, alpha);
            blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, textureWidth, textureHeight);
        }
    }

    public class BackButton extends CyberwareButton {

        public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/back.png");
        public BackButton(int pX, int pY) {
            super(pX, pY, 20, 18);
            texture = TEXTURE;
            textureWidth = 24;
            textureHeight = 24;
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            this.setBlitOffset(60);
            RenderSystem.enableDepthTest();
            pPoseStack.pushPose();
            pPoseStack.translate(0, 0, 100);
            super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
            pPoseStack.popPose();
        }

        @Override
        public void update() {

        }

        @Override
        List<Component> getTooltip() {
            return null;
        }

        @Override
        public void onPress() {
            if(CyberwareScreen.this.state == State.SUB) {
                CyberwareScreen.this.updateText(Component.translatable("tooltip.cybernetics.section"));
                CyberwareScreen.this.clearAll();
                CyberwareScreen.this.updateState();
                CyberwareScreen.this.scheduleTask(6, () -> {
                    CyberwareScreen.this.menu.switchActiveSlots(null);
                    CybPackets.sendToServer(new C2SSwitchActiveSlotPacket());
                    CyberwareScreen.this.menu.switchInventoryPage(-1);
                    CybPackets.sendToServer(new C2SSwitchPagePacket());
                });
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        @Override
        public void onEvent(GuiEvent event) {

        }
    }

    public class SectionButton extends CyberwareButton implements ITransparentWidget {

        private final CyberwareSection section;
        private final ResourceLocation id;
        private final ResourceLocation texture;
        public SectionButton(ResourceLocation id, CyberwareSection section) {
            super(leftPos + section.getType().getX(), topPos + section.getType().getY() + 20, 24, 24);
            this.section = section;
            this.texture = new ResourceLocation(id.getNamespace(), "textures/gui/cyberware/section/" + id.getPath() + ".png");
            this.id = id;
            alpha = 0.0f;
            visible = false;
            active = false;
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            visible = alpha > 0.0f;
            active = visible;
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
            RenderSystem.setShaderColor(color, color, color, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
//            int u = this.isHoveredOrFocused() ? width : 0;

            enableScissor(CyberwareScreen.this.boxLeft, CyberwareScreen.this.boxTop, CyberwareScreen.this.boxRight, CyberwareScreen.this.boxBottom);
            blit(pPoseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
            disableScissor();
        }

        @Override
        public void update() {

        }

        @Override
        public void onEvent(GuiEvent event) {
            if(event instanceof StateEvent stateEvent) {
                if(stateEvent.getState() == State.TRANSITION_SUB) {
                    CyberwareScreen.this.alphaWidget(this, 0.0f, 10);
                    CyberwareScreen.this.scheduleTask(15, () -> {
                        this.y = topPos + this.section.getType().getY() + 20;
                    });
                }
            }
        }

        @Override
        List<Component> getTooltip() {
            return null;
        }

        @Override
        public void onPress() {
            if(CyberwareScreen.this.state == State.MAIN) {
                CyberwareScreen.this.updateText(Component.translatable("tooltip." + id.getNamespace() + ".section." + id.getPath()));
                CyberwareScreen.this.clearAll();
                CyberwareScreen.this.updateState(section.getType().getXOffset(), section.getType().getYOffset());
                CyberwareScreen.this.scheduleTask(15, () -> {
                    CyberwareScreen.this.menu.switchActiveSlots(section.getType());
                    CybPackets.sendToServer(new C2SSwitchActiveSlotPacket(section.getType()));
                    CyberwareScreen.this.menu.switchInventoryPage(0);
                    CybPackets.sendToServer(new C2SSwitchPagePacket(0));
                });
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }

        @Override
        public float getTransparency() {
            return alpha;
        }

        @Override
        public void setTransparency(float alpha) {
            this.alpha = alpha;
        }
    }
}
