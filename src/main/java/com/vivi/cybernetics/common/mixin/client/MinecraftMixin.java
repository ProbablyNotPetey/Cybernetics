package com.vivi.cybernetics.common.mixin.client;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.shader.ScannerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void cybernetics$shouldEntityAppearGlowing(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(ScannerRenderer.shouldRenderGlowingEntiy(entity)) {
            cir.setReturnValue(true);
        }
    }
}
