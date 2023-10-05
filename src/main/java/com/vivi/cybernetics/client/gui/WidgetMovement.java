package com.vivi.cybernetics.client.gui;

import com.vivi.cybernetics.util.Easing;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.Mth;

public class WidgetMovement {

    private final AbstractWidget widget;
    private final int oldX;
    private final int oldY;
    private final int newX;
    private final int newY;
    private final long startTime;
    private final int duration;
    private final Easing easing;
    private boolean isDone = false;

    public WidgetMovement(AbstractWidget widget, int newX, int newY, long startTime, int duration) {
        this(widget, newX, newY, startTime, duration, Easing.LINEAR);
    }
    public WidgetMovement(AbstractWidget widget, int newX, int newY, long startTime, int duration, Easing easing) {
        this.widget = widget;
        this.oldX = widget.x;
        this.oldY = widget.y;
        this.newX = newX;
        this.newY = newY;
        this.startTime = startTime;
        this.duration = duration;
        this.easing = easing;
    }

    //this is called every render frame!
    public void update(long currentTime, float partialTicks) {
        float normalizedTime = (currentTime - startTime) + partialTicks;
        float percent = normalizedTime / duration;
        if(percent >= 1.0f) {
            isDone = true;
            return;
        }
        float easedPercent = easing.ease(percent);
        widget.x = (int)Mth.lerp(easedPercent, oldX, newX);
        widget.y = (int)Mth.lerp(easedPercent, oldY, newY);
    }

    public boolean isDone() {
        return isDone;
    }

}
