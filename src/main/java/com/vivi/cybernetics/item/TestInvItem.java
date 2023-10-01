package com.vivi.cybernetics.item;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.menu.PlayerCyberwareMenu;
import com.vivi.cybernetics.registry.ModCapabilities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class TestInvItem extends Item {
    public TestInvItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.getCapability(ModCapabilities.PLAYER_CYBERWARE).ifPresent(cyberware -> {
            if(!level.isClientSide && player instanceof ServerPlayer) {
                try {
                    NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer) -> new PlayerCyberwareMenu(pContainerId, pPlayerInventory, cyberware)), Component.literal(("Cyberware"))));
                    Cybernetics.LOGGER.info("Opening cyberware menu...");
                }
                catch(Exception e) {
                    Cybernetics.LOGGER.error("Could not open Cyberware menu", e);
                }
            }
        });


        return InteractionResultHolder.success(stack);
    }
}
