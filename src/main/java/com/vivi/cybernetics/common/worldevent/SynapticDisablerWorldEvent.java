package com.vivi.cybernetics.common.worldevent;

import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.client.worldevent.SynapticDisablerWorldEventRenderer;
import com.vivi.cybernetics.common.registry.CybWorldEvents;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class SynapticDisablerWorldEvent extends WorldEventInstance {

    private Vector3f pos;
    private int lifetime;
    private float radius;

    private int age;
    private int lightColor;
    private LodestoneRenderType outerRenderType;
    private LodestoneRenderType innerRenderType;
    public SynapticDisablerWorldEvent() {
        super(CybWorldEvents.SYNAPTIC_DISABLER);
    }

    public SynapticDisablerWorldEvent setPosition(Vector3f pos) {
        this.pos = pos;
        return this;
    }
    public SynapticDisablerWorldEvent setLifetime(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public SynapticDisablerWorldEvent setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public boolean isClientSynced() {
        return true;
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putFloat("x", pos.x());
        tag.putFloat("y", pos.y());
        tag.putFloat("z", pos.z());
        tag.putInt("lifetime", lifetime);
        tag.putFloat("radius", radius);
        return super.serializeNBT(tag);
    }

    @Override
    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        this.pos = new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
        this.lifetime = tag.getInt("lifetime");
        this.radius = tag.getFloat("radius");
        return super.deserializeNBT(tag);
    }

    @Override
    public void start(Level level) {
        this.age = 0;
        if(level.isClientSide) {
            outerRenderType = (LodestoneRenderType) SynapticDisablerWorldEventRenderer.SPHERE.apply(SynapticDisablerWorldEventRenderer.TEXTURE);
            innerRenderType = (LodestoneRenderType) SynapticDisablerWorldEventRenderer.SPHERE.apply(SynapticDisablerWorldEventRenderer.TEXTURE);
        }
    }

    @Override
    public void tick(Level level) {
        if(age == lifetime) {
            end(level);
        }
//        Cybernetics.LOGGER.info("Ticking!");

        if(level.isClientSide) {
            //this doesnt work for some reason idrk
            //todo: fix
            BlockPos bPos = new BlockPos((int)pos.x(), (int)pos.y(), (int)pos.z());
            this.lightColor = level.hasChunkAt(bPos) ? LevelRenderer.getLightColor(level, bPos) : 0;
        }


        age++;
    }

    @Override
    public void end(Level level) {
        super.end(level);
        if(level.isClientSide) {
            RenderHelper.removeRenderType(outerRenderType);
            RenderHelper.removeRenderType(innerRenderType);
        }
    }

    public Vector3f getPosition() {
        return pos;
    }

    //returns 0 on the server!
    public int getLightColor() {
        return lightColor;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getAge() {
        return age;
    }

    public float getRadius() {
        return radius;
    }

    public LodestoneRenderType getOuterRenderType() {
        return outerRenderType;
    }
    public LodestoneRenderType getInnerRenderType() {
        return innerRenderType;
    }
}
