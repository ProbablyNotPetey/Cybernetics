package com.vivi.cybernetics.item;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;


public class CyberwareItem extends Item {
    private final List<CyberwareItem> requirements = new ArrayList<>();
    private final List<CyberwareItem> incompatibilities = new ArrayList<>();
    public CyberwareItem(Properties pProperties) {
        super(pProperties);
    }

    public CyberwareItem addRequirements(Item... requirements) {
        for(Item item : requirements) {
            this.requirements.add((CyberwareItem) item);
        }
        return this;
    }
    public CyberwareItem addIncompatibilities(Item... incompatibilities) {
        for(Item item : incompatibilities) {
            this.incompatibilities.add((CyberwareItem) item);
        }
        return this;
    }

    public List<CyberwareItem> getRequirements() {
        return requirements;
    }

    public List<CyberwareItem> getIncompatibilities() {
        return incompatibilities;
    }

    public void cyberwareTick(ItemStack stack, Level level, Player player) {

    }

    public void onEquip(ItemStack stack, Level level, Player player) {

    }

    public void onUnequip(ItemStack stack, Level level, Player player) {

    }
}