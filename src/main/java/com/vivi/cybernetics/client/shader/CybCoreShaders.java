package com.vivi.cybernetics.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Cybernetics.MOD_ID)
public class CybCoreShaders {
    private static final List<ShaderHolder> shaders = new ArrayList<>();


    //todo: rewrite this shader entirely
    public static ShaderHolder
            SCAN = register(new ResourceLocation(Cybernetics.MOD_ID, "scan"), DefaultVertexFormat.POSITION_TEX, "DepthBuffer", "InvViewMat", "InvProjMat", "CameraPos", "Center", "Radius"),
            CIRCLE_PROGRESS = register(new ResourceLocation(Cybernetics.MOD_ID, "circle_progress"), DefaultVertexFormat.POSITION_TEX, "Progress"),
            BLAST_WAVE = register(new ResourceLocation(Cybernetics.MOD_ID, "blast_wave"), DefaultVertexFormat.PARTICLE, "Duration", "Time"),
            FALLING_PARTICLE = register(new ResourceLocation(Cybernetics.MOD_ID, "falling_particle"), DefaultVertexFormat.PARTICLE),
            SYNAPTIC_DISABLER = register(new ResourceLocation(Cybernetics.MOD_ID, "synaptic_disabler"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)

    ;


    public static ShaderHolder register(ResourceLocation location, VertexFormat vertexFormat, String... uniformsToCache) {
        ShaderHolder holder = new ShaderHolder(location, vertexFormat, uniformsToCache);
        shaders.add(holder);
        return holder;
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        for (ShaderHolder shader : shaders) {
            LodestoneShaderRegistry.registerShader(event, shader.createInstance(event.getResourceProvider()));
        }
    }
}
