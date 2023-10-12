package com.vivi.cybernetics.client.gui.util;

import com.vivi.cybernetics.util.client.Easing;
import net.minecraft.util.Mth;

public class WidgetScale {
    private final IScalableWidget widget;
    private final float oldScale;
    private final float newScale;
    private final long startTime;
    private final int duration;
    private final Easing easing;
    private boolean isDone = false;

    public WidgetScale(IScalableWidget widget, float newScale, long startTime, int duration) {
        this(widget, newScale, startTime, duration, Easing.LINEAR);
    }
    public WidgetScale(IScalableWidget widget, float newScale, long startTime, int duration, Easing easing) {
        this.widget = widget;
        this.oldScale = widget.getScale();
        this.newScale = newScale;
        this.startTime = startTime;
        this.duration = duration;
        this.easing = easing;
    }

    //this is called every render frame!
    public void update(long currentTime, float partialTicks) {
        float normalizedTime = (currentTime - startTime) + partialTicks;
        float percent = normalizedTime / duration;
        if(percent >= 1.0f) {
            widget.setScale(newScale);
            isDone = true;
            return;
        }
        float easedPercent = easing.ease(percent);
        widget.setScale(Mth.lerp(easedPercent, oldScale, newScale));
    }

    public boolean isDone() {
        return isDone;
    }
}
