package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.IScalableWidget;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class EntityWidget extends CybAbstractWidget implements IScalableWidget {

    private float scale;
    private float rotation;
    private final Entity entity;

    private int boxLeft;
    private int boxRight;
    private int boxTop;
    private int boxBottom;
    public EntityWidget(int pX, int pY, float scale, Entity entity) {
        super(pX, pY, (int) scale, (int) (scale*2), Component.empty());
        this.scale = scale;
        this.rotation = 0.0f;
        this.entity = entity;
        this.playSound = false;
    }

    public EntityWidget setBox(int boxLeft, int boxRight, int boxTop, int boxBottom) {
        this.boxLeft = boxLeft;
        this.boxRight = boxRight;
        this.boxTop = boxTop;
        this.boxBottom = boxBottom;
        return this;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

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
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if(!this.visible) return;
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.enableScissor(boxLeft, boxTop, boxRight, boxBottom);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 150);
        RenderHelper.renderEntity(entity, guiGraphics.pose(), getX() + scale/2, getY() + scale*2, 20, scale, rotation);
        guiGraphics.pose().popPose();
        //7.5f * (float)Math.cos((getGameTime() - startTime + pPartialTick) / 40.0f)
        guiGraphics.disableScissor();
    }
}
