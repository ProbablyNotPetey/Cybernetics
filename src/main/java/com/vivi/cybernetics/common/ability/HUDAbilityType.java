package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HUDAbilityType extends AbilityType {

    public HUDAbilityType() {
        super(new ResourceLocation(Cybernetics.MOD_ID, "textures/item/mk1_optics.png"));
    }

    @Override
    public void tick(Ability ability, Level level, Player player) {
        super.tick(ability, level, player);
        if(!(ability.isEnabled() && level.isClientSide)) return;
        if(!CyberneticsHUD.getInstance().isEnabled()) {
            CyberneticsHUD.getInstance().enable();
        }
    }

    @Override
    public void onDisable(Ability ability, Level level, Player player) {
        super.onDisable(ability, level, player);
        if(!level.isClientSide) return;
        CyberneticsHUD.getInstance().disable();
    }
}
