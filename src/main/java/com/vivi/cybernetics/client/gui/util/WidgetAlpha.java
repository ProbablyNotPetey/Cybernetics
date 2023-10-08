package com.vivi.cybernetics.client.gui.util;

import com.vivi.cybernetics.util.Easing;
import net.minecraft.util.Mth;

public class WidgetAlpha {

    private final ITransparentWidget widget;
    private final float oldAlpha;
    private final float newAlpha;
    private final long startTime;
    private final int duration;
    private final Easing easing;
    private boolean isDone = false;

    public WidgetAlpha(ITransparentWidget widget, float newAlpha, long startTime, int duration) {
        this(widget, newAlpha, startTime, duration, Easing.LINEAR);
    }
    public WidgetAlpha(ITransparentWidget widget, float newAlpha, long startTime, int duration, Easing easing) {
        this.widget = widget;
        this.oldAlpha = widget.getAlpha();
        this.newAlpha = newAlpha;
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
        widget.setAlpha(Mth.lerp(easedPercent, oldAlpha, newAlpha));
    }

    public boolean isDone() {
        return isDone;
    }

}
