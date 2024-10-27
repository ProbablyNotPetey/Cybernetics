package com.vivi.cybernetics.datagen;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vivi.cybernetics.common.registry.CybRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CyberwareStationRecipeBuilder implements RecipeBuilder {
    private Item result;
    private int count;
    private Ingredient input;
    private final RecipeCategory category = RecipeCategory.MISC;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();


    public CyberwareStationRecipeBuilder(ItemLike result, int count) {
        this.result = result.asItem();
        this.count = count;
    }


    public static CyberwareStationRecipeBuilder recipe(ItemLike result) {
        return new CyberwareStationRecipeBuilder(result, 1);
    }
    public static CyberwareStationRecipeBuilder recipe(ItemLike result, int count) {
        return new CyberwareStationRecipeBuilder(result, count);
    }


    public CyberwareStationRecipeBuilder input(ItemLike input) {
        return input(Ingredient.of(input));
    }
    public CyberwareStationRecipeBuilder input(TagKey<Item> input) {
        return input(Ingredient.of(input));
    }
    public CyberwareStationRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }


    public CyberwareStationRecipeBuilder ingredient(ItemLike item) {
        return ingredient(Ingredient.of(item));
    }
    public CyberwareStationRecipeBuilder ingredient(TagKey<Item> item) {
        return ingredient(Ingredient.of(item));
    }
    public CyberwareStationRecipeBuilder ingredient(Ingredient item) {
        this.ingredients.add(item);
        return this;
    }


    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }


    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new Result(id, result, count, input, ingredients, advancement, new ResourceLocation(id.getNamespace(), "recipes/" + this.category.getFolderName() + "/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation pId) {
        if(this.ingredients.size() > 6) {
            throw new IllegalStateException("Too many ingredients for recipe " + pId);
        }
        if(input == null) {
            throw new IllegalStateException("No input defined for recipe " + pId);
        }
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final Ingredient input;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item result, int count, Ingredient input, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.input = input;
            this.ingredients = ingredients;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {

            json.add("input", input.toJson());

            JsonArray ingredientsJson = new JsonArray();
            ingredients.forEach(ingredient -> {
                ingredientsJson.add(ingredient.toJson());
            });
            json.add("ingredients", ingredientsJson);
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if(this.count > 1) {
                resultJson.addProperty("count", this.count);
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CybRecipeTypes.CYBERWARE_CRAFTING_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}