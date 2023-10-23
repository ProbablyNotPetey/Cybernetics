package com.vivi.cybernetics.common.mixin;

import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getBlockJumpFactor", at = @At("HEAD"), cancellable = true)
    public void cybernetics$getBlockJumpFactor(CallbackInfoReturnable<Float> cir) {
        Entity e = (Entity) (Object) this;
        if(e instanceof Player player) {
            if(CyberwareHelper.hasCyberwareItem(player, CybItems.FULL_SPEED_FEET.get())) {
                cir.setReturnValue(1.0f);
            }
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("HEAD"), cancellable = true)
    public void cybernetics$getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
        Entity e = (Entity) (Object) this;
        if(e instanceof Player player) {
            if(CyberwareHelper.hasCyberwareItem(player, CybItems.FULL_SPEED_FEET.get())) {
                cir.setReturnValue(1.0f);
            }
        }
    }
}
