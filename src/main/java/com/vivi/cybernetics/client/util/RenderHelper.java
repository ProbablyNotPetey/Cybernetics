package com.vivi.cybernetics.client.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.util.Maath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RenderHelper {

    public static void renderEntity(Entity entity, PoseStack poseStack, float x, float y, float scale, float rotation) {
        renderEntity(entity, poseStack, x, y, 50.0f, scale, rotation);
    }

    /**
     * A lot of this was adopted from The One Probe by McJty!!! I have no idea what I'm doing lol
     */
    public static void renderEntity(Entity entity, PoseStack poseStack, float x, float y, float z, float scale, float rotation) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(-scale, scale, scale);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));

        //store entity rotations
        float xRot = entity.getXRot();
        float xRotO = entity.xRotO;
        float yRot = entity.getYRot();
        float yRotO = entity.yRotO;

        float yBodyRotO = 0.0f;
        float yBodyRot = 0.0f;
        float yHeadRot = 0.0f;
        float yHeadRotO = 0.0f;

        //modify entity rotations
        entity.setXRot(0.0F);
        entity.xRotO = 0.0F;
        entity.setYRot(0.0F);
        entity.yRotO = 0.0F;

        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            yBodyRotO = livingEntity.yBodyRotO;
            yBodyRot = livingEntity.yBodyRot;
            yHeadRot = livingEntity.yHeadRot;
            yHeadRotO = livingEntity.yHeadRotO;
            livingEntity.yBodyRotO = 0.0F;
            livingEntity.yBodyRot = 0.0F;
            livingEntity.yHeadRot = 0.0F;
            livingEntity.yHeadRotO = 0.0F;
        }


        RenderSystem.applyModelViewMatrix();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            Quaternion quaternion = Vector3f.XP.rotationDegrees(180.0f);
            quaternion.conj();
            dispatcher.overrideCameraOrientation(quaternion);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            dispatcher.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> {
                dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack, buffer, LightTexture.FULL_BRIGHT);
            });
            buffer.endBatch();
        }
        catch(Exception e) {
            Cybernetics.LOGGER.error("Could not render entity!", e);
        }

        //restore entity rotations
        entity.setXRot(xRot);
        entity.xRotO = xRotO;
        entity.setYRot(yRot);
        entity.yRotO = yRotO;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.yBodyRotO = yBodyRotO;
            livingEntity.yBodyRot = yBodyRot;
            livingEntity.yHeadRot = yHeadRot;
            livingEntity.yHeadRotO = yHeadRotO;
        }

        dispatcher.setRenderShadow(true);
        poseStack.popPose();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();


    }

    /**
     * Adapted from <a href="https://github.com/TheComputerizer/The-Impossible-Library/blob/1.19.2-forge/src/main/java/mods/thecomputerizer/theimpossiblelibrary/util/client/GuiUtil.java">The Impossible Library</a>
     * <p>
     * Color is ARGB!
     * </p>
     */
    public static void drawLine(Vector3f start, Vector3f end, Vector4f color, float width, float offset) {
        double angle = Math.toDegrees(Math.atan2(start.y() - end.y(), start.x() - end.x()));
        Vector3f start1 = Maath.getVertex(start,width/2d,Math.toRadians(angle+90d));
        Vector3f start2 = Maath.getVertex(start,width/2d,Math.toRadians(angle-90d));
        Vector3f end1 = Maath.getVertex(end,width/2d,Math.toRadians(angle-90d));
        Vector3f end2 = Maath.getVertex(end,width/2d,Math.toRadians(angle+90d));
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.vertex(start1.x(), start1.y(), offset).color(color.x(), color.y(), color.z(), color.w()).endVertex();
        builder.vertex(start2.x(), start2.y(), offset).color(color.x(), color.y(), color.z(), color.w()).endVertex();
        builder.vertex(end1.x(), end1.y(), offset).color(color.x(), color.y(), color.z(), color.w()).endVertex();
        builder.vertex(end2.x(), end2.y(), offset).color(color.x(), color.y(), color.z(), color.w()).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void resetShaderColor() {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
