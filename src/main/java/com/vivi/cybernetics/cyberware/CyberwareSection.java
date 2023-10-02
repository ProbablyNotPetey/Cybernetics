package com.vivi.cybernetics.cyberware;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CyberwareSection extends ItemStackHandler {

    public final ResourceLocation id;
    public final TagKey<Item> tag;

    public CyberwareSection(ResourceLocation id, int size, TagKey<Item> tag) {
        super(size);
        this.id = id;
        this.tag = tag;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return (stack.getItem() instanceof CyberwareItem) && (stack.is(tag) || stack.is(ModTags.ANY)) && super.isItemValid(slot, stack);
//        return super.isItemValid(slot, stack);
    }
}
