package com.vivi.cybernetics.datagen;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void onGatherDataEvent(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


        BlockTagGenerator blockTags = new BlockTagGenerator(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeClient(), new ItemModelGenerator(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new CyberwarePropertiesGenerator(output, existingFileHelper));
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagGenerator(output, event.getLookupProvider(), blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new CyberwareRecipesGenerator(output));

    }
}
