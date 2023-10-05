package com.vivi.cybernetics.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class ModAbstractWidget extends AbstractWidget {
    protected boolean playSound = true;
    public ModAbstractWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
    }

    /**
     * Identical to superclass except checks if playSound = true, allows widgets to not play the sound if they dont want to
     */
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(pButton)) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    if(playSound) this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(pMouseX, pMouseY);
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
