package com.vivi.cybernetics.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {


    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void cybernetics$renderEffects(GuiGraphics guiGraphics, CallbackInfo ci) {
        if(CyberneticsHUD.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}
