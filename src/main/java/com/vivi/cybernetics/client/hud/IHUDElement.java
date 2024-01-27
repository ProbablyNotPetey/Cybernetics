package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public interface IHUDElement extends StringRepresentable {

    void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight);
    void init();
}
