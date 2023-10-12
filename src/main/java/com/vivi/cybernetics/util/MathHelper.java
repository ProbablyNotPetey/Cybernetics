package com.vivi.cybernetics.util;

import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;

public class MathHelper {


    /**
     * Adapted from <a href="https://github.com/TheComputerizer/The-Impossible-Library/blob/1.19.2-forge/src/main/java/mods/thecomputerizer/theimpossiblelibrary/util/MathUtil.java">The Impossible Library</a>
     */
    public static Vector3f getVertex(Vector3f center, double radius, double angle) {
        return new Vector3f((float)(center.x()+(radius*Math.cos(angle))),(float) (center.y()+(radius*Math.sin(angle))),0);
    }

    public static float toRadius(float x, float y) {
        return Mth.sqrt(x*x + y*y);
    }
    public static float toAngle(float x, float y) {
        return (float) Mth.atan2(y, x);
    }
}
