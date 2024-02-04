package com.vivi.cybernetics.common.ability;

import com.vivi.cybernetics.client.shader.ScannerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ScanAbilityType extends AbilityType {

    public ScanAbilityType() {
        super(10, 0);
    }

    @Override
    public void onEnable(Ability ability, Level level, Player player) {
        super.onEnable(ability, level, player);
        if(!level.isClientSide) return;
        ScannerRenderer.getInstance().setup(player, 200);
    }
}
