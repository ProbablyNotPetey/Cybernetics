package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IHUDElement {

    void render(PoseStack poseStack, float partialTick);
}
