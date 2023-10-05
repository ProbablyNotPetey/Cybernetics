package com.vivi.cybernetics.client.gui.util;

import net.minecraft.network.chat.Component;

public abstract class AbstractScalableWidget extends ModAbstractWidget {
    protected float scale;
    public AbstractScalableWidget(int pX, int pY, int pWidth, int pHeight, float scale, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
