package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CybCreativeTabs{

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cybernetics.MOD_ID);

        public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("cybernetics",
            () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.cybernetics")).icon(() ->
                            new ItemStack(CybItems.MK2_OPTICS.get()))
                    .displayItems((parameters, output) -> {
                        CybItems.ITEMS.getEntries().forEach((item) -> output.accept(item.get()));
                        CybBlocks.BLOCKS.getEntries().forEach(block -> {
                            output.accept(block.get().asItem());
                        });
                    }
                    ).build());

    public static final void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
