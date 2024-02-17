package com.vivi.cybernetics.common.worldevent;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.registry.CybWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.helpers.block.BlockPosHelper;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class SynapticDisablerWorldEvent extends WorldEventInstance {

    private BlockPos pos;
    private int lifetime;
    private int age;
    public SynapticDisablerWorldEvent() {
        super(CybWorldEvents.SYNAPTIC_DISABLER);
    }

    public SynapticDisablerWorldEvent setData(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }
    public SynapticDisablerWorldEvent setPosition(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public boolean isClientSynced() {
        return true;
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putLong("pos", pos.asLong());
        tag.putInt("lifetime", lifetime);
        return super.serializeNBT(tag);
    }

    @Override
    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        this.pos = BlockPos.of(tag.getLong("pos"));
        this.lifetime = tag.getInt("lifetime");
        return super.deserializeNBT(tag);
    }

    @Override
    public void start(Level level) {
        this.age = 0;
    }

    @Override
    public void tick(Level level) {
        if(age == lifetime) {
            end(level);
        }



        age++;
    }

    public BlockPos getPosition() {
        return pos;
    }
}
