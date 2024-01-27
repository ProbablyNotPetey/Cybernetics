package com.vivi.cybernetics.client.util;


import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

/**
 * Doesn't actually extend PositionedScreenshakeInstance cuz you can't do super.super.updateIntensity()
 */
public class CustomPositionedScreenshakeInstance extends ScreenshakeInstance {

    public final Vec3 position;
    public final boolean alwaysApply;
    public final float falloffDistance;
    public final float maxDistance;
    public final Easing falloffEasing;
    public CustomPositionedScreenshakeInstance(int duration, boolean alwaysApply, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
        this.alwaysApply = alwaysApply;
    }

    public CustomPositionedScreenshakeInstance(int duration, boolean alwaysApply, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, alwaysApply, position, falloffDistance, maxDistance, Easing.LINEAR);
    }


    @Override
    public float updateIntensity(Camera camera, RandomSource random) {
        float intensity = super.updateIntensity(camera, random);
        float distance = (float) position.distanceTo(camera.getPosition());
        if (distance > maxDistance) {
            return 0;
        }
        float distanceMultiplier = 1;
        if (distance > falloffDistance) {
            float remaining = maxDistance - falloffDistance;
            float current = distance - falloffDistance;
            distanceMultiplier = 1 - current / remaining;
        }
        Vector3f lookDirection = camera.getLookVector();
        Vec3 directionToScreenshake = position.subtract(camera.getPosition()).normalize();
        //this is the only change
        float angle = alwaysApply ? 1.0f : Math.max(0, lookDirection.dot(new Vector3f(directionToScreenshake.toVector3f())));
        Cybernetics.LOGGER.info("Angle: " + angle);
        return ((intensity * distanceMultiplier) + (intensity * angle)) * 0.5f;
    }
}
