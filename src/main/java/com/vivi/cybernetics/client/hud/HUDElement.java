package com.vivi.cybernetics.client.hud;

public abstract class HUDElement implements IHUDElement {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    public HUDElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
