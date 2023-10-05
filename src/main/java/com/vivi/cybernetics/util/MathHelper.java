package com.vivi.cybernetics.util;

import com.mojang.math.Vector3f;

public class MathHelper {


    /**
     * Adapted from <a href="https://github.com/TheComputerizer/The-Impossible-Library/blob/1.19.2-forge/src/main/java/mods/thecomputerizer/theimpossiblelibrary/util/MathUtil.java">The Impossible Library</a>
     */
    public static Vector3f getVertex(Vector3f center, double radius, double angle) {
        return new Vector3f((float)(center.x()+(radius*Math.cos(angle))),(float) (center.y()+(radius*Math.sin(angle))),0);
    }

}
