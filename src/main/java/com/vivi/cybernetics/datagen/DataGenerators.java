package com.vivi.cybernetics.datagen;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void onGatherDataEvent(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new ItemModelGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CyberwarePropertiesGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ItemTagGenerator(generator, new BlockTagsProvider(generator), event.getExistingFileHelper()));

    }
}
