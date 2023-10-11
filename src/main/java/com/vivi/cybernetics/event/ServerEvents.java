package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.ability.Ability;
import com.vivi.cybernetics.capability.PlayerAbilities;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.CybItems;
import com.vivi.cybernetics.registry.CybTags;
import com.vivi.cybernetics.util.AbilityHelper;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        CyberwareHelper.getCyberware(player).ifPresent(cyberware -> {
            for(int i = 0; i < cyberware.getSlots(); i++) {
                if (cyberware.getStackInSlot(i).getItem() instanceof CyberwareItem item) {
                    item.cyberwareTick(cyberware.getStackInSlot(i), player.level, player);
                }
            }
        });
        AbilityHelper.getAbilities(player).ifPresent(PlayerAbilities::tickAbilities);
//        AbilityHelper.getAbilities(player).ifPresent(abilities -> {
//            if(event.side.isClient()) {
//                Cybernetics.LOGGER.info("Client abilities: " + abilities.serializeNBT());
//            }
//        });
    }

    @SubscribeEvent
    public static void onHarvestCheckEvent(PlayerEvent.HarvestCheck event) {
        //net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), pBlock)
        if(CyberwareHelper.hasCyberwareItem(event.getEntity(), CybItems.STONE_MINING_FISTS.get())) {
            BlockState block = event.getTargetBlock();
            event.setCanHarvest(TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, block));
        }
    }

    @SubscribeEvent
    public static void onBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
        if(CyberwareHelper.hasCyberwareItem(event.getEntity(), CybItems.STONE_MINING_FISTS.get())) {
            BlockState block = event.getState();
            ItemStack stack = event.getEntity().getMainHandItem();
            if(stack.getItem().getDestroySpeed(stack, block) <= 1.0F) {
                event.setNewSpeed(TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, block) ? event.getOriginalSpeed() * 4.0F : event.getOriginalSpeed());
            }

        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getEntity().level.isClientSide) return;
        Player player = (Player) event.getEntity();

        if(player.isCreative() || player.isSpectator()) return;

        if(CyberwareHelper.hasCyberwareItem(player, CybItems.EMERGENCY_DEFIBRILLATOR.get()) && !player.getCooldowns().isOnCooldown(CybItems.EMERGENCY_DEFIBRILLATOR.get())) {
            event.setCanceled(true);
            player.setHealth(player.getMaxHealth() / 4.0f);
            //play some sound
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2));
            player.getCooldowns().addCooldown(CybItems.EMERGENCY_DEFIBRILLATOR.get(), 2400);
        }
    }

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        if(event.getEntity().level.isClientSide) return;
        if(!(event.getEntity() instanceof Player player)) return;

        if(CyberwareHelper.hasCyberwareItem(player, CybItems.PROJECTILE_DEFLECTOR.get()) && event.getSource() instanceof IndirectEntityDamageSource) {
            EntityType<?> entityType = event.getSource().getDirectEntity().getType();
            if(!entityType.is(CybTags.PROJECTILES_ALWAYS_HIT) && player.getRandom().nextInt(10) < 4) {
                event.setCanceled(true);
            }
        }
    }

}
