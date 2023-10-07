package com.vivi.cybernetics.mixin;

import com.vivi.cybernetics.registry.ModItems;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getBlockJumpFactor", at = @At("HEAD"), cancellable = true)
    public void cybernetics_getBlockJumpFactor(CallbackInfoReturnable<Float> cir) {
        Entity e = (Entity) (Object) this;
        if(e instanceof Player player) {
            if(CyberwareHelper.hasCyberwareItem(player, ModItems.FULL_SPEED_FEET.get())) {
                cir.setReturnValue(1.0f);
            }
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("HEAD"), cancellable = true)
    public void cybernetics_getBlockSpeedFactor(CallbackInfoReturnable<Float> cir) {
        Entity e = (Entity) (Object) this;
        if(e instanceof Player player) {
            if(CyberwareHelper.hasCyberwareItem(player, ModItems.FULL_SPEED_FEET.get())) {
                cir.setReturnValue(1.0f);
            }
        }
    }
}
