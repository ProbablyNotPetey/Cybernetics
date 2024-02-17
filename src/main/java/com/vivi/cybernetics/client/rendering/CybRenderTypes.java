package com.vivi.cybernetics.client.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;

public class CybRenderTypes extends RenderStateShard {

    //LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE is quad based
    public static final RenderTypeProvider SPHERE_ADDITIVE_TEXTURE = new RenderTypeProvider((texture) ->
            LodestoneRenderTypeRegistry.createGenericRenderType("cybernetics:triangle_additive_texture",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLES, LodestoneRenderTypeRegistry.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                    .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
                    .setTextureState(texture)
                    .setCullState(CULL)));

    public static final RenderTypeProvider SPHERE_TEXTURE = new RenderTypeProvider((texture) ->
            LodestoneRenderTypeRegistry.createGenericRenderType("cybernetics:triangle_texture",
                    DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLES, LodestoneRenderTypeRegistry.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
                            .setTextureState(texture)
                            .setCullState(CULL)));


    private CybRenderTypes(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }
}
