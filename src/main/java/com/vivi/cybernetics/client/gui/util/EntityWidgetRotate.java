package com.vivi.cybernetics.client.gui.util;

import com.vivi.cybernetics.client.gui.CyberwareScreen;
import com.vivi.cybernetics.client.util.Easing;
import net.minecraft.util.Mth;

public class EntityWidgetRotate {
    private final CyberwareScreen.EntityWidget widget;
    private final float oldRotation;
    private final float newRotation;
    private final long startTime;
    private final int duration;
    private final Easing easing;
    private boolean isDone = false;

    public EntityWidgetRotate(CyberwareScreen.EntityWidget widget, float newRotation, long startTime, int duration) {
        this(widget, newRotation, startTime, duration, Easing.LINEAR);
    }
    public EntityWidgetRotate(CyberwareScreen.EntityWidget widget, float newRotation, long startTime, int duration, Easing easing) {
        this.widget = widget;
        this.oldRotation = widget.getRotation();
        this.newRotation = newRotation;
        this.startTime = startTime;
        this.duration = duration;
        this.easing = easing;
    }

    //this is called every render frame!
    public void update(long currentTime, float partialTicks) {
        float normalizedTime = (currentTime - startTime) + partialTicks;
        float percent = normalizedTime / duration;
        if(percent >= 1.0f) {
            widget.setRotation(newRotation);
            isDone = true;
            return;
        }
        float easedPercent = easing.ease(percent);
        widget.setRotation(Mth.lerp(easedPercent, oldRotation, newRotation));
    }

    public boolean isDone() {
        return isDone;
    }
}
