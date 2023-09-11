package com.vivi.cybernetics.compat.jei;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.CyberwareStationScreen;
import com.vivi.cybernetics.menu.CyberwareStationMenu;
import com.vivi.cybernetics.recipe.CyberwareStationRecipe;
import com.vivi.cybernetics.registry.ModBlocks;
import com.vivi.cybernetics.registry.ModMenuTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class CyberneticsJeiPlugin implements IModPlugin {

    public static RecipeType<CyberwareStationRecipe> CYBERWARE_CRAFTING_TYPE
            = new RecipeType<>(CyberwareStationRecipeCategory.UID, CyberwareStationRecipe.class);


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Cybernetics.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CyberwareStationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CyberwareStationRecipe> cyberwareCraftingRecipes = recipeManager.getAllRecipesFor(CyberwareStationRecipe.Type.INSTANCE);
        registration.addRecipes(CYBERWARE_CRAFTING_TYPE, cyberwareCraftingRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CYBERWARE_STATION_BLOCK.get()), CYBERWARE_CRAFTING_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CyberwareStationScreen.class, 31, 58, 105, 14, CYBERWARE_CRAFTING_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CyberwareStationMenu.class, ModMenuTypes.CYBERWARE_STATION_MENU.get(), CYBERWARE_CRAFTING_TYPE, 0, 7, 8, 36);
    }
}
