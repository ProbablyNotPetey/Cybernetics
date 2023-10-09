package com.vivi.cybernetics.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.gui.event.CybGuiEventListener;
import com.vivi.cybernetics.client.gui.event.GuiEvent;
import com.vivi.cybernetics.util.Easing;
import com.vivi.cybernetics.util.ScheduledTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class CybAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected final List<WidgetMovement> widgetsToMove = new ArrayList<>();
    protected final List<WidgetScale> widgetsToScale = new ArrayList<>();
    protected final List<WidgetAlpha> widgetsToAlpha = new ArrayList<>();
    protected final List<ScheduledTask> tasks = new ArrayList<>();
    protected long time;



    public CybAbstractContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        time = 0L;
        clearAll();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        time++;
        for(int i = 0; i < tasks.size(); i++) {
            ScheduledTask task = tasks.get(i);
            if(task.continuous() && time >= task.startTime() && (task.endTime() == -1 || time <= task.endTime())) {
                task.task().run();
            }
            else if(task.endTime() != -1 && time > task.endTime()) {
                tasks.remove(i);
                i--;
            }
            else if(time == task.startTime()) {
                task.task().run();
                tasks.remove(i);
                i--;
            }
        }
        tasks.forEach(task -> {
//            Cybernetics.LOGGER.info("continuous: " + task.continuous() + ", start: " + (time >= task.startTime()) + ", end: " + (time <= task.endTime()) + ", endTime is -1: " + (task.endTime() == -1));

        });
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
        for(int i = 0; i < widgetsToAlpha.size(); i++) {
            WidgetAlpha alphaWidget = widgetsToAlpha.get(i);
            alphaWidget.update(currentGameTime, partialTick);
            if(alphaWidget.isDone()) {
                widgetsToAlpha.remove(i);
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
    public void scaleWidget(IScalableWidget widget, float scale, int duration) {
        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration));
    }
    public void scaleWidget(IScalableWidget widget, float scale, int duration, Easing easing) {
        widgetsToScale.add(new WidgetScale(widget, scale, getGameTime(), duration, easing));
    }
    public void alphaWidget(ITransparentWidget widget, float alpha, int duration) {
        widgetsToAlpha.add(new WidgetAlpha(widget, alpha, getGameTime(), duration));
    }
    public void alphaWidget(ITransparentWidget widget, float alpha, int duration, Easing easing) {
        widgetsToAlpha.add(new WidgetAlpha(widget, alpha, getGameTime(), duration, easing));
    }
    public void scheduleTask(int time, Runnable task) {
        tasks.add(new ScheduledTask(this.time + time, -1, task, false));
    }
    public void scheduleTask(int time, Runnable task, boolean continuous) {
        tasks.add(new ScheduledTask(this.time + time, -1, task, continuous));
    }
    public void scheduleTask(int time, int endTime, Runnable task) {
        tasks.add(new ScheduledTask(this.time + time, time + endTime, task, true));
    }

    public void clearAll() {
        widgetsToMove.clear();
        widgetsToScale.clear();
        widgetsToAlpha.clear();
        tasks.clear();
    }

    public void broadcastGuiEvent(GuiEvent event) {
        this.renderables.forEach(renderable -> {
            if(renderable instanceof CybGuiEventListener listener) {
                listener.onEvent(event);
            }
        });
        this.onEvent(event);
    }

    public void onEvent(GuiEvent event) {

    }



    public float getPartialTick() {
        return Minecraft.getInstance().getPartialTick();
    }
    public long getGameTime() {
        return Minecraft.getInstance().player.level.getGameTime();
    }
}
