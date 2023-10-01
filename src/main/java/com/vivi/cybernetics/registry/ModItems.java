package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.item.MobEffectCyberware;
import com.vivi.cybernetics.item.TestInvItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.ImmutableTriple;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cybernetics.MOD_ID);

    public static final RegistryObject<Item>
            TEST_INV_ITEM = ITEMS.register("test_inv_item", () -> new TestInvItem(new Item.Properties().tab(Cybernetics.TAB))),
            TEST_CYBERWARE_ITEM = ITEMS.register("test_cyberware_item", () -> new CyberwareItem(cyberwareProps())),
            NIGHT_VISION_EYES = ITEMS.register("night_vision_eyes", () -> new MobEffectCyberware(cyberwareProps(), new ImmutableTriple<>(MobEffects.NIGHT_VISION, 320, 0)))
    ;


    private static Item.Properties cyberwareProps() {
        return new Item.Properties().stacksTo(1).tab(Cybernetics.TAB);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
