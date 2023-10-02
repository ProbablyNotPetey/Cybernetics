package com.vivi.cybernetics.item;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CyberwareItem extends Item {


    /** Ingredients instead of individual Items to support tags/or
     * <ul>
     *     Examples:
     *     <li>{ Ingredient.of(foo), Ingredient.of(bar) } matches both foo and bar</li>
     *     <li>{ Ingredient.of(foo), Ingredient.of(bar, baz) } matches foo and either bar or baz</li>
     * </ul>
     */
    private final List<Ingredient> requirements = new ArrayList<>();
    private final List<Ingredient> incompatibilities = new ArrayList<>();
    public CyberwareItem(Properties pProperties) {
        super(pProperties);
    }

    //These must be added AFTER registration!!! Common Setup is preferable, theoretically could be done on server reload
    public CyberwareItem addRequirements(Ingredient... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
        return this;
    }
    public CyberwareItem addIncompatibilities(Ingredient... incompatibilities) {
        this.incompatibilities.addAll(Arrays.asList(incompatibilities));
        return this;
    }

    public List<Ingredient> getRequirements() {
        return requirements;
    }

    public List<Ingredient> getIncompatibilities() {
        return incompatibilities;
    }

    public void cyberwareTick(ItemStack stack, Level level, Player player) {

    }

    public void onEquip(ItemStack stack, Level level, Player player) {

    }

    public void onUnequip(ItemStack stack, Level level, Player player) {

    }
}