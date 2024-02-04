package com.vivi.cybernetics.common.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.shader.ScannerRenderer;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.item.CyberwareItem;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.registry.CybItems;
import com.vivi.cybernetics.common.registry.CybMobEffects;
import com.vivi.cybernetics.common.registry.CybTags;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.common.util.CyberwareHelper;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.s2c.S2CToggleBerserkPacket;
import com.vivi.cybernetics.server.network.packet.s2c.S2CToggleHUDPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
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
                    item.cyberwareTick(cyberware.getStackInSlot(i), player.level(), player);
                }
            }
        });
        AbilityHelper.getAbilities(player).ifPresent(PlayerAbilities::tickAbilities);
        player.getCapability(Cybernetics.PLAYER_SPIKE).ifPresent(playerSpike -> {
            if(playerSpike.isSpiking()) {
                playerSpike.setTime(playerSpike.getTime() + 1);
            }
        });
        AbilityHelper.getAbilities(player).ifPresent(abilities -> {
//            Cybernetics.LOGGER.info(event.side.isClient() + ", " + abilities.serializeNBT());
        });
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if(!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }
        player.getCapability(Cybernetics.PLAYER_SPIKE).ifPresent(playerSpike -> {
            if(playerSpike.isSpiking()) event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if(event.getEntity().level().isClientSide) return;
        LivingEntity entity = event.getEntity();
        if(entity.hasEffect(CybMobEffects.PARALYZED.get())) {
            entity.setJumping(false);
        }
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
        if(event.getEntity().level().isClientSide) return;
        Player player = (Player) event.getEntity();

        if(!(player.isCreative() || player.isSpectator())) {
            if(AbilityHelper.enableAbility(player, CybAbilities.EMERGENCY_DEFIBRILLATOR.get(), true)) {
                event.setCanceled(true);
                return;
            }
        }

        //disables all abilities on death
        AbilityHelper.getAbilities(player).ifPresent(abilities -> {
            abilities.getAbilities().forEach(ability -> {
                ability.disable(player);
            });
        });
    }

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        if(event.getEntity().level().isClientSide) return;
        if(!(event.getEntity() instanceof Player player)) return;

        if(CyberwareHelper.hasCyberwareItem(player, CybItems.PROJECTILE_DEFLECTOR.get()) && event.getSource().isIndirect()) {
            EntityType<?> entityType = event.getSource().getDirectEntity().getType();
            if(!entityType.is(CybTags.PROJECTILES_ALWAYS_HIT) && player.getRandom().nextInt(10) < 4) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getLevel().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CybPackets.sendToClient(new S2CToggleHUDPacket(AbilityHelper.isEnabled(player, CybAbilities.HUD.get())), player);
        //todo: this should be based on a tag.
        CybPackets.sendToClient(new S2CToggleBerserkPacket(AbilityHelper.isEnabled(player, CybAbilities.MK1_BERSERK.get(), CybAbilities.MK2_BERSERK.get(), CybAbilities.MK3_BERSERK.get())), player);
    }

}
