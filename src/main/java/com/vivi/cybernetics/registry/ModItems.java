package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.TestInvItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cybernetics.MOD_ID);

    public static final RegistryObject<Item> TEST_INV_ITEM = ITEMS.register("test_inv_item", () -> new TestInvItem(new Item.Properties().tab(Cybernetics.TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
