package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyberneticsHUD implements IGuiOverlay {

    private final List<IHUDElement> elements = new ArrayList<>();
    private boolean isEnabled;
    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void addHUDElement(IHUDElement elememt) {
        elements.add(elememt);
        elememt.init();
    }
    public void addHUDElements(IHUDElement... elements) {
        for (IHUDElement element : elements) {
            this.elements.add(element);
            element.init();
        }
    }

    public List<IHUDElement> getElements() {
        return elements;
    }
    public IHUDElement getElement(String name) {
        for(IHUDElement element : elements) {
            if(element.getSerializedName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if(!isEnabled) return;
        elements.forEach(element -> element.render(poseStack, partialTick, screenWidth, screenHeight));
    }






    //singleton stuff
    private CyberneticsHUD() {}
    private static final CyberneticsHUD INSTANCE = new CyberneticsHUD();
    public static CyberneticsHUD getInstance() {
        return INSTANCE;
    }
}
