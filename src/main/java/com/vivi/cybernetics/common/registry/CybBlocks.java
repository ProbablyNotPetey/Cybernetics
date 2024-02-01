package com.vivi.cybernetics.common.registry;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.common.block.CyberwareStationBlock;
import com.vivi.cybernetics.common.block.FreezerBlock;
import com.vivi.cybernetics.common.block.SurgicalChamberBlock;
import com.vivi.cybernetics.common.block.entity.CyberwareStationBlockEntity;
import com.vivi.cybernetics.common.block.entity.FreezerBlockEntity;
import com.vivi.cybernetics.common.block.entity.SurgicalChamberBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.ToIntFunction;

public class CybBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Cybernetics.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Cybernetics.MOD_ID);

    public static final RegistryObject<Block>
            FREEZER_BLOCK = BLOCKS.register("freezer", () -> new FreezerBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL))),
            CYBERWARE_STATION_BLOCK = BLOCKS.register("cyberware_station", () -> new CyberwareStationBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.5F).lightLevel(litBlockEmission(5)))),
            SURGICAL_CHAMBER_BLOCK = BLOCKS.register("surgical_chamber", () -> new SurgicalChamberBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(3.5F)))
    ;

    public static final RegistryObject<BlockEntityType<FreezerBlockEntity>> FREEZER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("freezer_block_entity", () -> BlockEntityType.Builder.of(FreezerBlockEntity::new, FREEZER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<CyberwareStationBlockEntity>> CYBERWARE_STATION_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("cyberware_station_block_entity", () -> BlockEntityType.Builder.of(CyberwareStationBlockEntity::new, CYBERWARE_STATION_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SurgicalChamberBlockEntity>> SURGICAL_CHAMBER_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("surgical_chamber_block_entity", () -> BlockEntityType.Builder.of(SurgicalChamberBlockEntity::new, SURGICAL_CHAMBER_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    //registers block items for every block registered
    public static void registerBlockItems(final RegisterEvent event) {
        BLOCKS.getEntries().forEach(block -> {
            event.register(ForgeRegistries.Keys.ITEMS,
                    helper -> helper.register(new ResourceLocation(Objects.requireNonNull(block.getId().toString())), new BlockItem(block.get(), new Item.Properties())));
        });
    }

    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (state) -> {
            return state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
        };
    }
}
