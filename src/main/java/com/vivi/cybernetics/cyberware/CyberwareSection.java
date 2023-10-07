package com.vivi.cybernetics.cyberware;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.CybTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CyberwareSection extends ItemStackHandler {

    //Mod devs can probably just add elements to this list to change the sorting.
    public static final List<ResourceLocation> SECTION_SORT = new ArrayList<>();

    static {
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "head"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "eyes"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "upper_organs"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "lower_organs"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "skeleton"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "skin"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "hands"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "arms"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "legs"));
        SECTION_SORT.add(new ResourceLocation(Cybernetics.MOD_ID, "feet"));
    }

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
}
