package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.client.hud.CyberneticsHUD;
import com.vivi.cybernetics.common.registry.CybItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HUDAbilityType extends AbilityType {

    public HUDAbilityType() {
        super(CybItems.MK1_OPTICS.get());
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
