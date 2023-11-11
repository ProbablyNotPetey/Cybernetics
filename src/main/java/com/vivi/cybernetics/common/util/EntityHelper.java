package com.vivi.cybernetics.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityHelper {


//    public static int getHeightAboveBlock(Entity entity) {
//
//        BlockPos pos = entity.blockPosition();
//        BlockHitResult hitResult = entity.level.clip(new ClipContext(new Vec3(pos.getX(), pos.getY(), pos.getZ()), new Vec3(pos.getX(), entity.level.getMinBuildHeight(), pos.getZ()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
//        if(hitResult.getType() == HitResult.Type.MISS) {
//            return -1;
//        }
//        return pos.subtract(hitResult.getBlockPos()).getY();
//
//    }
}
