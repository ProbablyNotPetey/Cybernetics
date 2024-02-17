package com.vivi.cybernetics.client.worldevent;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.rendering.CybRenderTypes;
import com.vivi.cybernetics.common.worldevent.SynapticDisablerWorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class SynapticDisablerWorldEventRenderer extends WorldEventRenderer<SynapticDisablerWorldEvent> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/stone.png");
    private static final RenderType SPHERE = CybRenderTypes.SPHERE_TEXTURE.apply(TEXTURE);

    public SynapticDisablerWorldEventRenderer() {

    }

    @Override
    public boolean canRender(SynapticDisablerWorldEvent instance) {
        float renderSize = 3.0f;
        return Minecraft.getInstance().levelRenderer.getFrustum().isVisible(AABB.ofSize(instance.getPosition().getCenter(), renderSize, renderSize, renderSize));

    }

    @Override
    public void render(SynapticDisablerWorldEvent instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
//        Cybernetics.LOGGER.info("Rendering!");



        VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();
        Vec3 pos = instance.getPosition().getCenter();
        poseStack.pushPose();
        poseStack.translate(pos.x(), pos.y(), pos.z());
        builder.renderSphere(RenderHandler.DELAYED_RENDER.getBuffer(CybRenderTypes.SPHERE_ADDITIVE_TEXTURE.apply(TEXTURE)), poseStack, 3, 30, 30);
        poseStack.scale(-1, -1, -1);
        builder.renderSphere(RenderHandler.DELAYED_RENDER.getBuffer(CybRenderTypes.SPHERE_TEXTURE.apply(new ResourceLocation("textures/block/oak_planks.png"))), poseStack, 3f, 30, 30);
        poseStack.popPose();

    }

}
