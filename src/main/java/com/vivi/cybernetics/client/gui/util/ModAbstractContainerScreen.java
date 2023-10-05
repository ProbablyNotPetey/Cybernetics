package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class ModAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected final List<WidgetMovement> widgetsToMove = new ArrayList<>();
    protected final List<WidgetScale> widgetsToScale = new ArrayList<>();

    public ModAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float frameTimeDelta) {
        super.render(pPoseStack, pMouseX, pMouseY, frameTimeDelta);

        //on the client so this is fine
        long currentGameTime = getGameTime();
        float partialTick = getPartialTick();
        for(int i = 0; i < widgetsToMove.size(); i++) {
            WidgetMovement movement = widgetsToMove.get(i);
            movement.update(currentGameTime, partialTick);
            if(movement.isDone()) {
                widgetsToMove.remove(i);
                i--;
            }
        }
        for(int i = 0; i < widgetsToScale.size(); i++) {
            WidgetScale scale = widgetsToScale.get(i);
            scale.update(currentGameTime, partialTick);
            if(scale.isDone()) {
                widgetsToScale.remove(i);
                i--;
            }
        }
    }

    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration) {
        widgetsToMove.add(new WidgetMovement(widget, newX, newY, getGameTime(), duration));
    }
    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration, Easing easing) {
        widgetsToMove.add(new WidgetMovement(widget, newX, newY, getGameTime(), duration, easing));
    }
    public void scaleWidget(AbstractScalableWidget widget, float scale, int duration) {
        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration));
    }
    public void scaleWidget(AbstractScalableWidget widget, float scale, int duration, Easing easing) {
        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration, easing));
    }

    public float getPartialTick() {
        return Minecraft.getInstance().getPartialTick();
    }
    public long getGameTime() {
        return Minecraft.getInstance().player.level.getGameTime();
    }
}
