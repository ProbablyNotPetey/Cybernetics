package com.vivi.cybernetics.mixin;

import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModItems;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing);

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getMovementEmission", at = @At("HEAD"), cancellable = true)
    public void cybernetics_getMovementEmission(CallbackInfoReturnable<Entity.MovementEmission> cir) {
        //todo: this needs to run on client too
        this.getCapability(ModCyberware.CYBERWARE).ifPresent(cyberwareInventory -> {
            for(int i = 0; i < cyberwareInventory.getSlots(); i++) {
                if(cyberwareInventory.getStackInSlot(i).is(ModItems.SOUND_ABSORBENT_FEET.get())) {
                    if(this.random.nextInt(4) > 0) {
                        cir.setReturnValue(MovementEmission.NONE);
                    }
                }
            }
        });
    }
}
