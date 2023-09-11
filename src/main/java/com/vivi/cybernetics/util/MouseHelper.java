package com.vivi.cybernetics.util;

public class MouseHelper {

    public static boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(x - 1) && mouseX <= (double)(x + width + 1) && mouseY >= (double)(y - 1) && mouseY < (double)(y + height + 1);
    }
}
