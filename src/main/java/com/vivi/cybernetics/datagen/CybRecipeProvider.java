package com.vivi.cybernetics.datagen;


import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public abstract class CybRecipeProvider extends RecipeProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final boolean generateAdvancements;
    public CybRecipeProvider(PackOutput output) {
        this(output, true);
    }

    public CybRecipeProvider(PackOutput output, boolean generateAdvancements) {
        super(output);
        this.generateAdvancements = generateAdvancements;
    }


    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        buildRecipes((finishedRecipe) -> {
            if (!set.add(finishedRecipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + finishedRecipe.getId());
            } else {
                list.add(DataProvider.saveStable(pOutput, finishedRecipe.serializeRecipe(), this.recipePathProvider.json(finishedRecipe.getId())));
                if(generateAdvancements) {
                    JsonObject jsonobject = finishedRecipe.serializeAdvancement();
                    if (jsonobject != null) {
                        CompletableFuture<?> saveFuture = saveAdvancement(pOutput, finishedRecipe, jsonobject);
                        if(saveFuture != null) {
                            list.add(saveFuture);
                        }
                    }
                }
            }
        });
        return CompletableFuture.allOf(list.toArray(new CompletableFuture<?>[0]));
    }
}
