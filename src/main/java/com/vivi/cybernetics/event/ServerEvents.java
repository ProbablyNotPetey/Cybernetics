package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModItems;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        if(event.side.isClient()) return;
        Player player = event.player;
        CyberwareHelper.getCyberware(player).ifPresent(cyberware -> {
            for(int i = 0; i < cyberware.getSlots(); i++) {
                if (cyberware.getStackInSlot(i).getItem() instanceof CyberwareItem item) {
                    item.cyberwareTick(cyberware.getStackInSlot(i), player.level, player);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getEntity().level.isClientSide) return;
        Player player = (Player) event.getEntity();

        if(player.isCreative() || player.isSpectator()) return;

        if(CyberwareHelper.hasCyberwareItem(player, ModItems.EMERGENCY_DEFIBRILLATOR.get()) && !player.getCooldowns().isOnCooldown(ModItems.EMERGENCY_DEFIBRILLATOR.get())) {
            event.setCanceled(true);
            player.setHealth(player.getMaxHealth() / 4.0f);
            //play some sound
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2));
            player.getCooldowns().addCooldown(ModItems.EMERGENCY_DEFIBRILLATOR.get(), 2400);
        }

    }

}
