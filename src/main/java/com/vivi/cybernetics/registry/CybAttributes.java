package com.vivi.cybernetics.registry;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CybAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cybernetics.MOD_ID);

//    public static final RegistryObject<Attribute> CAPACITY = ATTRIBUTES.register("capacity", () -> new RangedAttribute("attribute.cybernetics.name.capacity", 0, 0, 1024).setSyncable(true));
    public static final RegistryObject<Attribute> MAX_CAPACITY = ATTRIBUTES.register("max_capacity", () -> new RangedAttribute("attribute.cybernetics.name.max_capacity", 50, 1, 1024).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    @SubscribeEvent
    public static void onModifyAttributesEvent(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> {
            ATTRIBUTES.getEntries().forEach(attribute -> {
                event.add(entity, attribute.get());
            });
        });
    }
}
