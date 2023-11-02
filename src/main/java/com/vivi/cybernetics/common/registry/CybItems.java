package com.vivi.cybernetics.common.registry;

import com.mojang.datafixers.util.Pair;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.item.*;
import com.vivi.cybernetics.common.util.Triple;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class CybItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cybernetics.MOD_ID);

    //Regular items
    public static final RegistryObject<Item>
            MOTOR = ITEMS.register("motor", () -> new Item(regularProps())),
            NEURAL_INTERFACE = ITEMS.register("neural_interface", () -> new Item(regularProps())),
            PRINTED_CIRCUIT_BOARD = ITEMS.register("printed_circuit_board", () -> new Item(regularProps()))
    ;



    //Cyberware
    public static final RegistryObject<Item>

            //HEAD
            CAPACITY_EXTENSION_HEAD = ITEMS.register("capacity_extension_head", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(CybAttributes.MAX_CAPACITY.get(), new AttributeModifier(UUID.fromString("2c54b860-db1f-4c59-bfd2-6cc7dfac1afe"), "Capacity Boost Head", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL)))),
            MK1_BERSERK = ITEMS.register("mk1_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK1_BERSERK)),
            MK2_BERSERK = ITEMS.register("mk2_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK2_BERSERK)),
            MK3_BERSERK = ITEMS.register("mk3_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK3_BERSERK)),

            //EYES
            MK1_OPTICS = ITEMS.register("mk1_optics", () -> new OpticsItem(cyberwareProps(), false)),
            MK2_OPTICS = ITEMS.register("mk2_optics", () -> new OpticsItem(cyberwareProps(), true)),
            MK3_OPTICS = ITEMS.register("mk3_optics", () -> new OpticsItem(cyberwareProps(), true)),
            NIGHT_VISION_EYES = ITEMS.register("night_vision_eyes", () -> new NightVisionEyesItem(cyberwareProps())),

            //UPPER ORGANS
            EMERGENCY_DEFIBRILLATOR = ITEMS.register("emergency_defibrillator", () -> new CyberwareItem(cyberwareProps())),
            OXYGEN_RECYCLER = ITEMS.register("oxygen_recycler", () -> new CyberwareItem(cyberwareProps())),
            HEALTH_BOOST_ORGANS = ITEMS.register("health_boost_organs", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("f6585ba8-be7a-403a-92ed-620872197db2"), "Health Boost Upper Organs", 5, AttributeModifier.Operation.ADDITION)))),

            //LOWER ORGANS
            STOMACH_FILTER = ITEMS.register("stomach_filter", () -> new StomachFilterItem(cyberwareProps())),
            SYNAPTIC_DISABLER = ITEMS.register("synaptic_disabler", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.SYNAPTIC_DISABLER)),


            //SKELETON
            REINFORCED_SKELETON = ITEMS.register("reinforced_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.ARMOR, new AttributeModifier(UUID.fromString("1a9c9f62-d28f-48fb-8c9e-b688046f7099"), "Skeleton Armor", 2.0, AttributeModifier.Operation.ADDITION))).hideIncompatibilities()),
            TITANIUM_SKELETON = ITEMS.register("titanium_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(),
                    Pair.of(Attributes.ARMOR, new AttributeModifier(UUID.fromString("d4490a2f-e05e-4ddf-beea-66ec7ca6e5f1"), "Skeleton Armor", 5.0, AttributeModifier.Operation.ADDITION)),
                    Pair.of(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("d4490a2f-e05e-4ddf-beea-66ec7ca6e5f1"), "Skeleton Armor Toughness", 2.0, AttributeModifier.Operation.ADDITION))
                    ).hideIncompatibilities()),
            HEALTH_BOOST_SKELETON = ITEMS.register("health_boost_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("b2014109-6700-4e83-9fc6-5250bd9d558c"), "Health Boost Skeleton", 5, AttributeModifier.Operation.ADDITION)))),
            KINETIC_DISCHARGER = ITEMS.register("kinetic_discharger", () -> new KineticDischargerItem(cyberwareProps())),

            //SKIN
            FIRE_RESISTANCE_SKIN = ITEMS.register("fire_resistance_skin", () -> new MobEffectCyberwareItem(cyberwareProps(), Triple.of(MobEffects.FIRE_RESISTANCE, 319, 0))),
            PROJECTILE_DEFLECTOR = ITEMS.register("projectile_deflector", () -> new CyberwareItem(cyberwareProps())),

            //ARMS



            //HANDS
            STRENGTH_HANDS = ITEMS.register("strength_hands", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("8d73fa62-e37a-4bea-8b8a-3f329bc6e108"), "Hand Attack Boost", 3.0, AttributeModifier.Operation.ADDITION)))),
            STONE_MINING_FISTS = ITEMS.register("stone_mining_fists", () -> new CyberwareItem(cyberwareProps())),


            //LEGS
            REINFORCED_TENDONS = ITEMS.register("reinforced_tendons", () -> new ReinforcedTendonsItem(cyberwareProps())),
            SPEED_LEGS = ITEMS.register("speed_legs", () -> new MobEffectCyberwareItem(cyberwareProps(), Triple.of(MobEffects.MOVEMENT_SPEED, 319, 1))),

            //FEET
            BIONIC_FEET = ITEMS.register("bionic_feet", () -> new CyberwareItem(cyberwareProps())),
            SOUND_ABSORBENT_FEET = ITEMS.register("sound_absorbent_feet", () -> new CyberwareItem(cyberwareProps())),
            JUMP_BOOST_FEET = ITEMS.register("jump_boost_feet", () -> new MobEffectCyberwareItem(cyberwareProps(), Triple.of(MobEffects.JUMP, 319, 2))),
            FULL_SPEED_FEET = ITEMS.register("full_speed_feet", () -> new CyberwareItem(cyberwareProps()))
    ;


    private static Item.Properties cyberwareProps() {
        return new Item.Properties().stacksTo(1).tab(Cybernetics.TAB);
    }
    private static Item.Properties regularProps() {
        return new Item.Properties().tab(Cybernetics.TAB);
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
