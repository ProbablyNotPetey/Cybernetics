package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.StringRepresentable;

public interface IHUDElement extends StringRepresentable {

    void render(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight);
    void init();
}
