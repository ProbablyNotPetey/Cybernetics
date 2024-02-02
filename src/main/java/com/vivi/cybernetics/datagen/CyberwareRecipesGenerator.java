package com.vivi.cybernetics.datagen;

/*
public class CyberwareRecipesGenerator extends CybRecipeProvider {
    public CyberwareRecipesGenerator(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        //Blocks
        ShapedRecipeBuilder.shaped(CybBlocks.CYBERWARE_STATION_BLOCK.get().asItem())
                .define('I', CybTags.INGOTS_STEEL)
                .define('C', CybItems.PRINTED_CIRCUIT_BOARD.get())
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("III")
                .pattern("CRC")
                .pattern("QQQ")
                .unlockedBy("has_printed_circuit_board", has(CybItems.PRINTED_CIRCUIT_BOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CybBlocks.SURGICAL_CHAMBER_BLOCK.get().asItem())
                .define('N', CybItems.NEURAL_INTERFACE.get())
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('P', CybItems.PRINTED_CIRCUIT_BOARD.get())
                .define('S', CybTags.INGOTS_STEEL)
                .pattern("SSS")
                .pattern("NNN")
                .pattern("RPR")
                .unlockedBy("has_neural_interface", has(CybItems.NEURAL_INTERFACE.get()))
                .save(consumer);

        //Crafting items
        ShapedRecipeBuilder.shaped(CybItems.PRINTED_CIRCUIT_BOARD.get())
                .define('R', Items.COMPARATOR)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('S', CybTags.INGOTS_STEEL)
                .pattern("RCR")
                .pattern("SSS")
                .unlockedBy("has_steel", has(CybTags.INGOTS_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CybItems.MOTOR.get())
                .define('S', CybTags.INGOTS_STEEL)
                .define('P', Items.PISTON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.INGOTS_COPPER)
                .pattern("SS")
                .pattern("PR")
                .pattern("CC")
                .unlockedBy("has_piston", has(Items.PISTON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CybItems.NEURAL_INTERFACE.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('P', CybItems.PRINTED_CIRCUIT_BOARD.get())
                .pattern("RRR")
                .pattern("CPC")
                .unlockedBy("has_printed_circuit_board", has(CybItems.PRINTED_CIRCUIT_BOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CybItems.LENS.get())
                .define('G', Tags.Items.GLASS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("GGG")
                .pattern("GRG")
                .pattern("GGG")
                .unlockedBy("has_glass", has(Tags.Items.GLASS))
                .save(consumer);

        //Cyberware

        CyberwareStationRecipeBuilder.recipe(CybItems.MK1_BERSERK.get())
                .input(CybItems.PRINTED_CIRCUIT_BOARD.get())
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(CybItems.STIMULANT_INJECTOR.get())
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(CybTags.INGOTS_STEEL)
                .ingredient(CybItems.FLUX_CORE.get())
                .ingredient(CybTags.INGOTS_STEEL)
                .save(consumer);

        CyberwareStationRecipeBuilder.recipe(CybItems.MK2_BERSERK.get())
                .input(CybItems.MK1_BERSERK.get())
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(CybItems.STIMULANT_INJECTOR.get())
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(CybTags.INGOTS_STEEL)
                .ingredient(CybItems.FLUX_CORE.get())
                .ingredient(CybTags.INGOTS_STEEL)
                .save(consumer);

        CyberwareStationRecipeBuilder.recipe(CybItems.MK3_BERSERK.get())
                .input(CybItems.MK2_BERSERK.get())
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(CybItems.STIMULANT_INJECTOR.get())
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(CybItems.FLUX_CORE.get())
                .ingredient(Tags.Items.INGOTS_NETHERITE)
                .ingredient(CybItems.FLUX_CORE.get())
                .save(consumer);



        CyberwareStationRecipeBuilder.recipe(CybItems.MK1_OPTICS.get())
                .input(CybItems.PRINTED_CIRCUIT_BOARD.get())
                .ingredient(Tags.Items.GEMS_AMETHYST)
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(Tags.Items.GEMS_AMETHYST)
                .ingredient(Tags.Items.DUSTS_REDSTONE)
                .ingredient(CybItems.LENS.get())
                .ingredient(Tags.Items.DUSTS_REDSTONE)
                .save(consumer);

        CyberwareStationRecipeBuilder.recipe(CybItems.MK2_OPTICS.get())
                .input(CybItems.MK1_OPTICS.get())
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(Tags.Items.GEMS_DIAMOND)
                .ingredient(CybItems.NEURAL_INTERFACE.get())
                .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .ingredient(CybItems.LENS.get())
                .ingredient(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .save(consumer);

        CyberwareStationRecipeBuilder.recipe(CybItems.NIGHT_VISION_EYES.get())
                .input(CybItems.PRINTED_CIRCUIT_BOARD.get())
                .ingredient(Tags.Items.DUSTS_REDSTONE)
                .ingredient(Items.GLOWSTONE)
                .ingredient(Tags.Items.DUSTS_REDSTONE)
                .ingredient(CybItems.LENS.get())
                .ingredient(CybItems.LENS.get())
                .ingredient(CybItems.LENS.get())
                .save(consumer);

    }
}
 */
