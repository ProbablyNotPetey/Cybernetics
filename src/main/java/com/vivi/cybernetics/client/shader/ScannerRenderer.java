package com.vivi.cybernetics.client.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;


public class ScannerRenderer {


    private static Vector3f entityPos;
    private static boolean shouldRender;
    private static int startTime;
    private static int duration;
    private static final List<Entity> entitiesToGlow = new ArrayList<>();


    public static void setup(Entity entity, int duration) {
        entityPos = new Vector3f(entity.position());
        shouldRender = true;
        startTime = -1;
        ScannerRenderer.duration = duration;
        entitiesToGlow.clear();
        entitiesToGlow.addAll(entity.level.getEntities(entity, entity.getBoundingBox().inflate(30)));
    }

    public static void stop() {
        shouldRender = false;
    }

    /**
     * Lot of this was based on Scannable by Sangar_, bc idk rendering stuff lol
     */
    public static void renderScan(PoseStack poseStack, Matrix4f projectionMatrix, int renderTick, float partialTick, Camera camera, Frustum frustum) {
        if(!shouldRender) return;

        if(startTime == -1) startTime = renderTick;
        int time = renderTick - startTime;
        if(time > duration) {
            stop();
            return;
        }

        ShaderInstance shader = CybShaders.getScanShader();
        if(shader == null) return;

        RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
        int width = target.width;
        int height = target.height;

//        Cybernetics.LOGGER.info("Width: " + width + ", Height: " + height);
//        Cybernetics.LOGGER.info("Window width: " + Minecraft.getInstance().getWindow().getWidth() + ", Window height: " + Minecraft.getInstance().getWindow().getHeight());
//        Cybernetics.LOGGER.info("Screen width: " + Minecraft.getInstance().getWindow().getScreenWidth() + ", Screen height: " + Minecraft.getInstance().getWindow().getScreenHeight());


        Matrix4f invViewMat = new Matrix4f(poseStack.last().pose());
        invViewMat.invert();
        Matrix4f invProjMat = new Matrix4f(RenderSystem.getProjectionMatrix());
        invProjMat.invert();
        Vec3 cameraPos = camera.getPosition();

        shader.setSampler("DepthBuffer", target.getDepthTextureId());
        shader.safeGetUniform("InvViewMat").set(invViewMat);
        shader.safeGetUniform("InvProjMat").set(invProjMat);
        shader.safeGetUniform("CameraPos").set(new Vector3f(cameraPos));
        shader.safeGetUniform("Center").set(entityPos);
        shader.safeGetUniform("Radius").set((time + partialTick) * 1.5f);

        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        ShaderInstance oldShader = RenderSystem.getShader();
        RenderSystem.setShader(() -> shader);
//        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderHelper.resetShaderColor();


        RenderSystem.backupProjectionMatrix();
        //sets the current projection matrix so that screen coords go from (0,0) to (width, height)
        RenderSystem.setProjectionMatrix(Matrix4f.orthographic(0, width, 0, height, 1, 100));

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(0, height, -50).uv(0, 0).endVertex();
        buffer.vertex(width, height, -50).uv(1, 0).endVertex();
        buffer.vertex(width, 0, -50).uv(1, 1).endVertex();
        buffer.vertex(0, 0, -50).uv(0, 1).endVertex();
        tesselator.end();

        //reset
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.setShader(() -> oldShader);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static boolean shouldRenderGlowingEntiy(Entity entity) {
        if(!(entity instanceof LivingEntity)) return false;
        return shouldRender && entitiesToGlow.contains(entity);
    }
}
