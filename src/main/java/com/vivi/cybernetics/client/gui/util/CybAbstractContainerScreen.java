package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.util.ScreenHelper;
import com.vivi.cybernetics.client.util.Easing;
import com.vivi.cybernetics.client.util.ScheduledTask;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class CybAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected long time;



    public CybAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        time = 0L;
//        clearAll();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        time++;
//        for(int i = 0; i < tasks.size(); i++) {
//            ScheduledTask task = tasks.get(i);
//            if(task.continuous() && time >= task.startTime() && (task.endTime() == -1 || time <= task.endTime())) {
//                task.task().run();
//            }
//            else if(task.endTime() != -1 && time > task.endTime()) {
//                tasks.remove(i);
//                i--;
//            }
//            else if(time == task.startTime()) {
//                task.task().run();
//                tasks.remove(i);
//                i--;
//            }
//        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float frameTimeDelta) {
        super.render(guiGraphics, pMouseX, pMouseY, frameTimeDelta);

        //on the client so this is fine
//        long currentGameTime = getGameTime();
//        float partialTick = getPartialTick();
//        for(int i = 0; i < widgetsToMove.size(); i++) {
//            WidgetMovement movement = widgetsToMove.get(i);
//            movement.update(currentGameTime, partialTick);
//            if(movement.isDone()) {
//                widgetsToMove.remove(i);
//                i--;
//            }
//        }
//        for(int i = 0; i < widgetsToScale.size(); i++) {
//            WidgetScale scale = widgetsToScale.get(i);
//            scale.update(currentGameTime, partialTick);
//            if(scale.isDone()) {
//                widgetsToScale.remove(i);
//                i--;
//            }
//        }
//        for(int i = 0; i < widgetsToAlpha.size(); i++) {
//            WidgetAlpha alphaWidget = widgetsToAlpha.get(i);
//            alphaWidget.update(currentGameTime, partialTick);
//            if(alphaWidget.isDone()) {
//                widgetsToAlpha.remove(i);
//                i--;
//            }
//        }
    }

    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration) {
//        widgetsToMove.add(new WidgetMovement(widget, newX, newY, getGameTime(), duration));
        moveWidget(widget, newX, newY, duration, Easing.LINEAR);
    }
    public void moveWidget(AbstractWidget widget, int newX, int newY, int duration, Easing easing) {
//        widgetsToMove.add(new WidgetMovement(widget, newX, newY, getGameTime(), duration, easing));
        ScreenHelper.addAnimation(this, () -> (float) widget.getX(), (newValue) -> widget.setX((int) (float) newValue), newX, duration, easing);
        ScreenHelper.addAnimation(this, () -> (float) widget.getY(), (newValue) -> widget.setY((int) (float) newValue), newY, duration, easing);
    }
    public void scaleWidget(IScalableWidget widget, float scale, int duration) {
//        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration));
        ScreenHelper.addAnimation(this, widget::getScale, widget::setScale, scale, duration);
    }
    public void scaleWidget(IScalableWidget widget, float scale, int duration, Easing easing) {
//        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration, easing));
        ScreenHelper.addAnimation(this, widget::getScale, widget::setScale, scale, duration, easing);
    }
    public void alphaWidget(ITransparentWidget widget, float alpha, int duration) {
//        widgetsToAlpha.add(new WidgetAlpha(widget, alpha, getGameTime(), duration));
        ScreenHelper.addAnimation(this, widget::getTransparency, widget::setTransparency, alpha, duration);
    }
    public void alphaWidget(ITransparentWidget widget, float alpha, int duration, Easing easing) {
//        widgetsToAlpha.add(new WidgetAlpha(widget, alpha, getGameTime(), duration, easing));
        ScreenHelper.addAnimation(this, widget::getTransparency, widget::setTransparency, alpha, duration, easing);
    }
    public void scheduleTask(int time, Runnable task) {
//        tasks.add(new ScheduledTask(this.time + time, -1, task, false));
        ScreenHelper.scheduleTask(this, time, task);
    }
    public void scheduleTask(int time, Runnable task, boolean continuous) {
//        tasks.add(new ScheduledTask(this.time + time, -1, task, continuous));
        ScreenHelper.scheduleTask(this, time, task, continuous);
    }
    public void scheduleTask(int time, int endTime, Runnable task) {
//        tasks.add(new ScheduledTask(this.time + time, this.time + endTime, task, true));
        ScreenHelper.scheduleTask(this, time, endTime, task);
    }

    public void clearAll() {
        ScreenHelper.clearAnimations(this);
        ScreenHelper.clearTasks(this);
//        widgetsToMove.clear();
//        widgetsToScale.clear();
//        widgetsToAlpha.clear();
//        tasks.clear();
    }



    public float getPartialTick() {
        return minecraft.getPartialTick();
    }
    public long getGameTime() {
        return minecraft.player.level().getGameTime();
    }
}
