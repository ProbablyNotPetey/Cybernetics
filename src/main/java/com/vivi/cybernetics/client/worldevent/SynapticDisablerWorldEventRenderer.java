package com.vivi.cybernetics.client.worldevent;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class SynapticDisablerWorldEventRenderer extends WorldEventRenderer<SynapticDisablerWorldEvent> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/crafting_table_front.png");
//    private static final RenderType SPHERE = CybRenderTypes.SPHERE_TEXTURE.apply(TEXTURE);
    private static final RenderType SPHERE = LodestoneRenderTypeRegistry.createGenericRenderType("cybernetics:synaptic_disabler_sphere",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneRenderTypeRegistry.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(CybCoreShaders.SYNAPTIC_DISABLER.getInstance()))
                .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                .setTextureState(TEXTURE)
                .setCullState(LodestoneRenderTypeRegistry.NO_CULL));


    public SynapticDisablerWorldEventRenderer() {

    }

    @Override
    public boolean canRender(SynapticDisablerWorldEvent instance) {
        float renderSize = instance.getRadius() + 1.0f;
        return Minecraft.getInstance().levelRenderer.getFrustum().isVisible(AABB.ofSize(new Vec3(instance.getPosition()), renderSize, renderSize, renderSize));

    }

    @Override
    public void render(SynapticDisablerWorldEvent instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
//        Cybernetics.LOGGER.info("Rendering!");

//        Cybernetics.LOGGER.info("Light color: " + instance.getLightColor());
        float age = (instance.getAge() + partialTicks) / instance.getLifetime();

        float radius = Mth.lerp(Easing.EXPO_OUT.ease(age, 0, 1, 1), 0.5f, instance.getRadius());

        float alpha = 1 - Easing.QUAD_IN_OUT.ease(Mth.clamp(Mth.inverseLerp(age, 0.5f, 1.0f), 0.0f, 1.0f), 0, 1, 1);

        VFXBuilders.WorldVFXBuilder builder = ExtendedWorldVFXBuilder.createCustomWorld()
                .setPosColorTexLightmapDefaultFormat()
                .setLight(instance.getLightColor())
                .setAlpha(alpha);
        Vector3f pos = instance.getPosition();
        poseStack.pushPose();
        poseStack.translate(pos.x(), pos.y(), pos.z());

        //temporary workaround for lodestone's broken depth sorting for rendertypes
//        poseStack.scale(-1, -1, -1);



        ((ExtendedWorldVFXBuilder) builder).renderQuadSphereNoTiling(RenderHandler.DELAYED_RENDER.getBuffer(SPHERE), poseStack, radius, 40, 40);
//        ((CustomWorldVFXBuilder) builder).renderQuadSphere(RenderHandler.DELAYED_RENDER.getBuffer(SPHERE), poseStack, 8.0f, 30, 30);
        poseStack.popPose();

    }

}
