package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AbilityScreen extends Screen {
    public AbilityScreen() {
        super(Component.literal("Abilities"));
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        drawTorus(poseStack, 40, 80, 0, 180);


        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void drawTorus(PoseStack poseStack, float innerRadius, float outerRadius) {
        drawTorus(poseStack, innerRadius, outerRadius, 0, 360);
    }
    private void drawTorus(PoseStack poseStack, float innerRadius, float outerRadius, float startAngle, float stopAngle) {
        float centerX = minecraft.getWindow().getGuiScaledWidth() / 2.0f;
        float centerY = minecraft.getWindow().getGuiScaledHeight() / 2.0f;

        int slices = 128;


        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0.0f);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tesselator.getBuilder();

        vertexBuffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION);
        Matrix4f matrix4f = poseStack.last().pose();

        float angle = Mth.DEG_TO_RAD * (360.0f / slices);
        for(int i = 0; i <= slices; i++) {

            float cos = Mth.cos(-angle * i);
            float sin = Mth.sin(-angle * i);
            vertexBuffer.vertex(matrix4f, outerRadius * cos, outerRadius * sin, 0).endVertex();
            vertexBuffer.vertex(matrix4f, innerRadius * cos, innerRadius * sin, 0).endVertex();
        }



        //SHAPES DRAW IN COUNTER CLOCKWISE MOTION!

//        vertexBuffer.vertex(matrix4f, 1.0f, 200.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 100.0f, 200.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 100.0f, 1.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 0.0f).endVertex();

        tesselator.end();


        poseStack.popPose();
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
