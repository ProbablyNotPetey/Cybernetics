package com.vivi.cybernetics.common.registry;

import com.mojang.datafixers.util.Pair;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.item.*;
import com.vivi.cybernetics.common.util.Triple;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class CybItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cybernetics.MOD_ID);

    //Regular items
    public static final RegistryObject<Item>
            MOTOR = basicItem("motor"),
            NEURAL_INTERFACE = basicItem("neural_interface"),
            PRINTED_CIRCUIT_BOARD = basicItem("printed_circuit_board"),
            LENS = basicItem("lens"),
            FLUX_CORE = basicItem("flux_core"),
            STIMULANT_INJECTOR = basicItem("stimulant_injector")
    ;

    //Capacity shards
    public static final RegistryObject<Item>
        MK1_CAPACITY_SHARD = ITEMS.register("mk1_capacity_shard", () -> new Item(cyberwareProps())),
        MK2_CAPACITY_SHARD = ITEMS.register("mk2_capacity_shard", () -> new Item(cyberwareProps())),
        MK3_CAPACITY_SHARD = ITEMS.register("mk3_capacity_shard", () -> new Item(cyberwareProps())),
        MK4_CAPACITY_SHARD = ITEMS.register("mk4_capacity_shard", () -> new Item(cyberwareProps()))

    ;



    //Cyberware
    public static final RegistryObject<Item>

            //HEAD
            OVERCAPACITY_HEAD = ITEMS.register("overcapacity_head", () -> new CyberwareItem(cyberwareProps())),
            CARBON_FIBER_SKULL = ITEMS.register("carbon_fiber_skull", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.ARMOR, new AttributeModifier(UUID.fromString("7588e022-9e81-4c44-85e8-82979dec2e0c"), "Armor Boost Head", 2.0, AttributeModifier.Operation.ADDITION)))),
            MK1_BERSERK = ITEMS.register("mk1_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK1_BERSERK)),
            MK2_BERSERK = ITEMS.register("mk2_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK2_BERSERK)),
            MK3_BERSERK = ITEMS.register("mk3_berserk", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.MK3_BERSERK)),
            MK1_DASH = ITEMS.register("mk1_dash", () -> new DashCyberwareItem(cyberwareProps(), false)),
            MK2_DASH = ITEMS.register("mk2_dash", () -> new DashCyberwareItem(cyberwareProps(), true)),


            //EYES
            MK1_OPTICS = ITEMS.register("mk1_optics", () -> new OpticsItem(cyberwareProps(), false)),
            MK2_OPTICS = ITEMS.register("mk2_optics", () -> new OpticsItem(cyberwareProps(), true)),
            MK3_OPTICS = ITEMS.register("mk3_optics", () -> new OpticsItem(cyberwareProps(), true)),
            NIGHT_VISION_EYES = ITEMS.register("night_vision_eyes", () -> new NightVisionEyesItem(cyberwareProps())),

            //UPPER ORGANS
            EMERGENCY_DEFIBRILLATOR = ITEMS.register("emergency_defibrillator", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.EMERGENCY_DEFIBRILLATOR)),
            OXYGEN_RECYCLER = ITEMS.register("oxygen_recycler", () -> new CyberwareItem(cyberwareProps())),
            HEALTH_BOOST_ORGANS = ITEMS.register("health_boost_organs", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("f6585ba8-be7a-403a-92ed-620872197db2"), "Health Boost Upper Organs", 5, AttributeModifier.Operation.ADDITION)))),

            //LOWER ORGANS
            STOMACH_FILTER = ITEMS.register("stomach_filter", () -> new StomachFilterItem(cyberwareProps())),
            SYNAPTIC_DISABLER = ITEMS.register("synaptic_disabler", () -> new SimpleAbilityCyberwareItem(cyberwareProps(), CybAbilities.SYNAPTIC_DISABLER)),
            MK1_DOUBLE_JUMP_ADDER = ITEMS.register("mk1_double_jump_adder", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(CybAttributes.DOUBLE_JUMPS.get(), new AttributeModifier(UUID.fromString("053bf72d-a9e1-4e3f-8373-e2491155f9f5"), "Lower Organs Double Jump Boost", 1.0, AttributeModifier.Operation.ADDITION)))),
            MK2_DOUBLE_JUMP_ADDER = ITEMS.register("mk2_double_jump_adder", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(CybAttributes.DOUBLE_JUMPS.get(), new AttributeModifier(UUID.fromString("a819279b-894b-4c33-bb05-74d5751859f6"), "Lower Organs Double Jump Boost", 2.0, AttributeModifier.Operation.ADDITION)))),

            //SKELETON
            REINFORCED_SKELETON = ITEMS.register("reinforced_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.ARMOR, new AttributeModifier(UUID.fromString("1a9c9f62-d28f-48fb-8c9e-b688046f7099"), "Skeleton Armor", 2.0, AttributeModifier.Operation.ADDITION)))),
            TITANIUM_SKELETON = ITEMS.register("titanium_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(),
                    Pair.of(Attributes.ARMOR, new AttributeModifier(UUID.fromString("d4490a2f-e05e-4ddf-beea-66ec7ca6e5f1"), "Skeleton Armor", 5.0, AttributeModifier.Operation.ADDITION)),
                    Pair.of(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("d4490a2f-e05e-4ddf-beea-66ec7ca6e5f1"), "Skeleton Armor Toughness", 2.0, AttributeModifier.Operation.ADDITION))
                    )),
            HEALTH_BOOST_SKELETON = ITEMS.register("health_boost_skeleton", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("b2014109-6700-4e83-9fc6-5250bd9d558c"), "Health Boost Skeleton", 5, AttributeModifier.Operation.ADDITION)))),
            KINETIC_DISCHARGER = ITEMS.register("kinetic_discharger", () -> new KineticDischargerItem(cyberwareProps())),

            //SKIN
            FIRE_RESISTANCE_SKIN = ITEMS.register("fire_resistance_skin", () -> new MobEffectCyberwareItem(cyberwareProps(), Triple.of(MobEffects.FIRE_RESISTANCE, 319, 0))),
            PROJECTILE_DEFLECTOR = ITEMS.register("projectile_deflector", () -> new CyberwareItem(cyberwareProps())),

            //ARMS
            RANGE_EXTENDER = ITEMS.register("range_extender", () -> new AttributeCyberwareItem(cyberwareProps(), Pair.of(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(UUID.fromString("7c79b65a-615b-4d40-8268-a33b51ae757d"), "Arm Reach Boost", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL)))),


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

    private static RegistryObject<Item> basicItem(String name) {
        return ITEMS.register(name, () -> new Item(regularProps()));
    }
}
