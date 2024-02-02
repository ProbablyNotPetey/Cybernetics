package com.vivi.cybernetics.client.hud;

import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
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

    public void removeHUDElement(IHUDElement element) {
        elements.remove(element);
    }
    public void removeHUDElementByName(String name) {
        for(int i = 0; i < elements.size(); i++) {
            if(elements.get(i).getSerializedName().equals(name)) {
                elements.remove(i);
                i--;
            }
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
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        PlayerAbilities abilities = AbilityHelper.getAbilities(Minecraft.getInstance().player).orElse(null);
        if(abilities == null || !abilities.hasAbility(CybAbilities.HUD.get())) return;
        if(!isEnabled) return;
        elements.forEach(element -> element.render(gui, guiGraphics, partialTick, screenWidth, screenHeight));
    }






    //singleton stuff
    private CyberneticsHUD() {}
    private static final CyberneticsHUD INSTANCE = new CyberneticsHUD();
    public static CyberneticsHUD getInstance() {
        return INSTANCE;
    }
}
