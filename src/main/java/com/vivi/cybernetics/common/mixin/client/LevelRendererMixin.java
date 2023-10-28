package com.vivi.cybernetics.common.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.shader.ScannerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final private RenderBuffers renderBuffers;
    @Shadow private PostChain entityEffect;

    @ModifyVariable(method = "renderEntity", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private MultiBufferSource cybernetics$modifyMultiBufferSource(MultiBufferSource original, Entity entity, double pCamX, double pCamY, double pCamZ, float partialTick, PoseStack pPoseStack) {
//        if(ScannerRenderer.shouldRenderGlowingEntiy(entity)) {
//
//            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
//            int color = 0xd1221f;
//            int r = color >> 16 & 255;
//            int g = color >> 8 & 255;
//            int b = color & 255;
//            outlinebuffersource.setColor(r, g, b, 255);
//
//            entityEffect.process(partialTick);
//            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
//
//            return outlinebuffersource;
//        }
        return original;
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void cybernetics$renderEntity(Entity entity, double pCamX, double pCamY, double pCamZ, float partialTick, PoseStack pPoseStack, MultiBufferSource multiBufferSource, CallbackInfo ci) {
        if(ScannerRenderer.shouldRenderGlowingEntiy(entity)) {
            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
            int color = 0xd1221f;
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            outlinebuffersource.setColor(r, g, b, 255);
        }
    }

}
