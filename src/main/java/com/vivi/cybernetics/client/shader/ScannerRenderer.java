package com.vivi.cybernetics.client.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;

import java.util.ArrayList;
import java.util.List;


public class ScannerRenderer {
    private static final ScannerRenderer INSTANCE = new ScannerRenderer();
    private Vector3f entityPos;
    private boolean shouldRender;
    private int startTime;
    private int duration;
    private final List<Entity> entitiesToGlow = new ArrayList<>();

    private ScannerRenderer() {
    }

    public static ScannerRenderer getInstance() {
        return INSTANCE;
    }

    public void setup(Entity entity, int duration) {
        entityPos = new Vector3f(entity.position().toVector3f());
        shouldRender = true;
        startTime = -1;
        this.duration = duration;
        entitiesToGlow.clear();
        entitiesToGlow.addAll(entity.level().getEntities(entity, entity.getBoundingBox().inflate(30)));
    }

    public void stop() {
        shouldRender = false;
    }

    /**
     * Lot of this was based on Scannable by Sangar_
     */
    public void renderScan(PoseStack poseStack, int renderTick, float partialTick, Camera camera, RenderTarget target) {
        if(!shouldRender) return;

        if(startTime == -1) startTime = renderTick;
        int time = renderTick - startTime;
        if(time > duration) {
            stop();
            return;
        }

        ExtendedShaderInstance shader = (ExtendedShaderInstance) CybCoreShaders.SCAN.getInstance().get();
        if(shader == null) return;

//        RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
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
        shader.safeGetUniform("CameraPos").set(new Vector3f(cameraPos.toVector3f()));
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
        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, width, 0, height, 1, 100), VertexSorting.ORTHOGRAPHIC_Z);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(width, 0, -50).uv(1, 0).endVertex();
        buffer.vertex(width, height, -50).uv(1, 1).endVertex();
        buffer.vertex(0, height, -50).uv(0, 1).endVertex();
        buffer.vertex(0, 0, -50).uv(0, 0).endVertex();
        tesselator.end();

        //reset
        shader.setUniformDefaults();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.setShader(() -> oldShader);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public boolean shouldRenderGlowingEntiy(Entity entity) {
        if(!(entity instanceof LivingEntity)) return false;
        return shouldRender && entitiesToGlow.contains(entity);
    }
}
