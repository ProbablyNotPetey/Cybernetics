package com.vivi.cybernetics.client.worldevent;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.vivi.cybernetics.client.rendering.ICybLevelRenderer;
import com.vivi.cybernetics.client.shader.CybCoreShaders;
import com.vivi.cybernetics.client.util.ExtendedWorldVFXBuilder;
import com.vivi.cybernetics.common.worldevent.SynapticDisablerWorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class SynapticDisablerWorldEventRenderer extends WorldEventRenderer<SynapticDisablerWorldEvent> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/crafting_table_front.png");
//    private static final RenderType SPHERE = CybRenderTypes.SPHERE_TEXTURE.apply(TEXTURE);
    public static final RenderTypeProvider SPHERE = new RenderTypeProvider(texture ->
        LodestoneRenderTypeRegistry.createGenericRenderType("cybernetics:synaptic_disabler_sphere",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneRenderTypeRegistry.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(CybCoreShaders.SYNAPTIC_DISABLER.getInstance()))
                .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                .setTextureState(texture)
                .setCullState(LodestoneRenderTypeRegistry.CULL)
        ));

    public static final LodestoneRenderType SPHERE_TYPE = (LodestoneRenderType) SPHERE.apply(TEXTURE);
    public SynapticDisablerWorldEventRenderer() {

    }

    @Override
    public boolean canRender(SynapticDisablerWorldEvent instance) {
        float renderSize = instance.getRadius() + 2.0f;
//        Cybernetics.LOGGER.info("Can render: " + Minecraft.getInstance().levelRenderer.getFrustum().isVisible(AABB.ofSize(new Vec3(instance.getPosition()), renderSize, renderSize, renderSize)));
        return Minecraft.getInstance().levelRenderer.getFrustum().isVisible(AABB.ofSize(new Vec3(instance.getPosition()), renderSize, renderSize, renderSize));
//        return true;
    }

    @Override
    public void render(SynapticDisablerWorldEvent instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
//        Cybernetics.LOGGER.debug("Rendering!");

        if(instance.getOuterRenderType() == null || instance.getInnerRenderType() == null) {
            instance.initRenderTypes();
        }

//        Cybernetics.LOGGER.info("Light color: " + instance.getLightColor());
        float age = (instance.getAge() + partialTicks) / instance.getLifetime();


        float radius = Mth.lerp(Easing.EXPO_OUT.ease(age, 0, 1, 1), 0.5f, instance.getRadius());

        float alpha = 1 - Easing.QUAD_IN_OUT.ease(Mth.clamp(Mth.inverseLerp(age, 0.5f, 1.0f), 0.0f, 1.0f), 0, 1, 1);

        VFXBuilders.WorldVFXBuilder builder = ExtendedWorldVFXBuilder.createCustomWorld()
                .setPosColorTexLightmapDefaultFormat()
                .setLight(instance.getLightColor())
                .setAlpha(alpha);
        Vector3f pos = instance.getPosition();


//        Cybernetics.LOGGER.debug("===============================================================");
//        Cybernetics.LOGGER.debug("Position: " + instance.getPosition());
//        Cybernetics.LOGGER.debug("Pose 1: \n" + new Matrix4f(poseStack.last().pose()).invert());
//        Cybernetics.LOGGER.debug("View matrix: \n" + RenderSystem.getInverseViewRotationMatrix());

        poseStack.pushPose();
        poseStack.translate(pos.x(), pos.y(), pos.z());

        RenderTarget depthRenderTarget = ((ICybLevelRenderer) Minecraft.getInstance().levelRenderer).cybernetics$getDepthRenderTarget();


        LodestoneRenderTypeRegistry.applyUniformChanges(instance.getOuterRenderType(), (instance1) -> {
            instance1.safeGetUniform("Center").set(pos.x(), pos.y(), pos.z());
            instance1.safeGetUniform("InvViewMat").set(new Matrix4f(poseStack.last().pose()).invert());
            instance1.safeGetUniform("CameraPos").set(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());
            instance1.setSampler("DepthBuffer", depthRenderTarget.getDepthTextureId());
        });
        LodestoneRenderTypeRegistry.applyUniformChanges(instance.getInnerRenderType(), (instance1) -> {
            instance1.safeGetUniform("Center").set(pos.x(), pos.y(), pos.z());
            instance1.safeGetUniform("InvViewMat").set(new Matrix4f(poseStack.last().pose()).invert());
            instance1.safeGetUniform("CameraPos").set(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());
            instance1.safeGetUniform("Inverted").set(1);
            instance1.setSampler("DepthBuffer", depthRenderTarget.getDepthTextureId());
        });

        ((ExtendedWorldVFXBuilder) builder).renderQuadSphereNoTiling(RenderHandler.DELAYED_RENDER.getBuffer(instance.getOuterRenderType()), poseStack, radius, 40, 40);
        poseStack.scale(-1, -1, -1);
//        poseStack.translate(-4, 0, 0);
        ((ExtendedWorldVFXBuilder) builder).renderQuadSphereNoTiling(RenderHandler.DELAYED_RENDER.getBuffer(instance.getInnerRenderType()), poseStack, radius, 40, 40);

//        Cybernetics.LOGGER.debug("Pose 2: \n" + poseStack.last().pose());

        poseStack.popPose();

    }

}
