package com.vivi.cybernetics.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.function.Supplier;

/**
 * Custom impl of LodestoneWorldParticleRenderType that allows passing null in for texture
 */
public class CustomWorldParticleRenderType extends LodestoneWorldParticleRenderType {
    public CustomWorldParticleRenderType(RenderType renderType, ShaderHolder shaderHolder, ResourceLocation texture, GlStateManager.SourceFactor srcAlpha, GlStateManager.DestFactor dstAlpha) {
        super(renderType, shaderHolder, texture, srcAlpha, dstAlpha);
    }
    public CustomWorldParticleRenderType(RenderType renderType, Supplier<ShaderInstance> shader, ResourceLocation texture, GlStateManager.SourceFactor srcAlpha, GlStateManager.DestFactor dstAlpha) {
        super(renderType, shader, texture, srcAlpha, dstAlpha);
    }


    @Override
    public void begin(BufferBuilder builder, TextureManager manager) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.setShader(shader);
        if(texture != null) {
            RenderSystem.setShaderTexture(0, texture);
        }
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }


}
