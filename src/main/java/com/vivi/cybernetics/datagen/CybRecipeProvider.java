package com.vivi.cybernetics.datagen;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
/*
public class CybRecipeProvider extends RecipeProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final boolean generateAdvancements;
    public CybRecipeProvider(DataGenerator pGenerator) {
        this(pGenerator, true);
    }

    public CybRecipeProvider(DataGenerator generator, boolean generateAdvancements) {
        super(generator);
        this.generateAdvancements = generateAdvancements;
    }


    @Override
    public void run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        buildCraftingRecipes((p_236366_) -> {
            if (!set.add(p_236366_.getId())) {
                throw new IllegalStateException("Duplicate recipe " + p_236366_.getId());
            } else {
                saveRecipe(pOutput, p_236366_.serializeRecipe(), this.recipePathProvider.json(p_236366_.getId()));
                if(generateAdvancements) {
                    JsonObject jsonobject = p_236366_.serializeAdvancement();
                    if (jsonobject != null) {
                        saveAdvancement(pOutput, jsonobject, this.advancementPathProvider.json(p_236366_.getAdvancementId()));
                    }
                }
            }
        });
    }

    private static void saveRecipe(CachedOutput pOutput, JsonObject pRecipeJson, Path pPath) {
        try {
            DataProvider.saveStable(pOutput, pRecipeJson, pPath);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save recipe {}", pPath, ioexception);
        }

    }
}
*/