package com.vivi.cybernetics.client.util;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Animation {


    private final Supplier<Float> getter;
    private final Consumer<Float> setter;
    private final float oldValue;
    private final float newValue;
    private final long startTime;
    private final int duration;
    private final Easing easing;
    private boolean isDone = false;

    public Animation(Supplier<Float> getter, Consumer<Float> setter, float newValue, long startTime, int duration) {
        this(getter, setter, newValue, startTime, duration, Easing.LINEAR);
    }
    public Animation(Supplier<Float> getter, Consumer<Float> setter, float newValue, long startTime, int duration, Easing easing) {
        this.getter = getter;
        this.setter = setter;
        this.oldValue = getter.get();
        this.newValue = newValue;
        this.startTime = startTime;
        this.duration = duration;
        this.easing = easing;
    }

    //this is called every render frame!
    public void update(long currentTime, float partialTicks) {
        float normalizedTime = (currentTime - startTime) + partialTicks;
        float percent = normalizedTime / duration;
        if(percent >= 1.0f) {
            setter.accept(newValue);
            isDone = true;
            return;
        }
        float easedPercent = easing.ease(percent, 0, 1, 1);
        setter.accept(Mth.lerp(easedPercent, oldValue, newValue));
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "Animation{" +
                "oldValue=" + oldValue +
                ", newValue=" + newValue +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
