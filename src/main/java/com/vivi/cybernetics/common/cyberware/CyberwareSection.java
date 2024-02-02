package com.vivi.cybernetics.common.cyberware;

import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.registry.CybTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareSection extends ItemStackHandler {


    private final ResourceLocation id;

    private final CyberwareSectionType type;

    public CyberwareSection(CyberwareSectionType type, ResourceLocation id) {
        super(type.getSize());
        this.type = type;
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public CyberwareSectionType getType() {
        return type;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return (stack.getItem() instanceof CyberwareItem) && (stack.is(type.getTag()) || stack.is(CybTags.ANY_SECTION)) && super.isItemValid(slot, stack);
//        return super.isItemValid(slot, stack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CyberwareSection that = (CyberwareSection) o;

        if (!id.equals(that.id)) return false;
        return type.equals(that.type);
    }
}
