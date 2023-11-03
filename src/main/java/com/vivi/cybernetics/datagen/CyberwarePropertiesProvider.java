package com.vivi.cybernetics.datagen;

import com.mojang.logging.LogUtils;
import com.vivi.cybernetics.server.data.CyberwareProperties;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ExistingFileHelper.ResourceType;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CyberwarePropertiesProvider implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceType PROPERTIES = new ResourceType(PackType.SERVER_DATA, ".json", "cyberware_properties");
    protected ExistingFileHelper existingFileHelper;
    private final DataGenerator.PathProvider pathProvider;
    private final Map<Item, CyberwareProperties> properties = new HashMap<>();

    public CyberwarePropertiesProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        this.existingFileHelper = existingFileHelper;
        this.pathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, "cyberware_properties");
    }

    @Override
    public void run(CachedOutput pOutput) throws IOException {
        properties.clear();
        addProperties();
        properties.forEach((item, properties) -> {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if(id == null) {
                throw new IllegalArgumentException("Unknown item: " + item.toString());
            }
            Path path = pathProvider.json(id);
            try {
                DataProvider.saveStable(pOutput, properties.serialize(), path);
            }
            catch(IOException e) {
                LOGGER.error("Couldn't save properties {}", path, e);
            }
        });
    }

    public abstract void addProperties();

    public CyberwareProperties add(Item item, Builder builder) {

        return properties.put(item, builder.build());
    }

    @Override
    public String getName() {
        return "Cyberware Properties";
    }

    public static class Builder {

        private List<Ingredient> requirements = new ArrayList<>();
        private List<Ingredient> incompatibilities = new ArrayList<>();
        private boolean showRequirements = true;
        private boolean showIncompatibilities = true;
        private boolean showDescription = true;
        private int capacity = 2;
        public Builder() {

        }

        public Builder addRequirements(Ingredient... requirements) {
            this.requirements.addAll(List.of(requirements));
            return this;
        }
        public Builder addRequirements(Object... requirements) {
            for(Object o : requirements) {
                Ingredient ingredient;
                if(o instanceof Item) {
                    ingredient = Ingredient.of((Item) o);
                }
                else if(o instanceof TagKey<?>) {
                    TagKey<?> tag = (TagKey<?>) o;
                    if(!tag.isFor(ForgeRegistries.ITEMS.getRegistryKey())) {
                        throw new IllegalArgumentException("Tag " + tag + " is not item tag!");
                    }
                    ingredient = Ingredient.of((TagKey<Item>) tag);
                }
                else {
                    throw new IllegalArgumentException("Must pass in a tag or an item!");
                }
                this.requirements.add(ingredient);
            }
            return this;
        }

        public Builder addIncompatibilities(Ingredient... incompatibilities) {
            this.incompatibilities.addAll(List.of(incompatibilities));
            return this;
        }
        public Builder addIncompatibilities(Object... incompatibilities) {
            for(Object o : incompatibilities) {
                Ingredient ingredient;
                if(o instanceof Item) {
                    ingredient = Ingredient.of((Item) o);
                }
                else if(o instanceof TagKey<?>) {
                    TagKey<?> tag = (TagKey<?>) o;
                    if(!tag.isFor(ForgeRegistries.ITEMS.getRegistryKey())) {
                        throw new IllegalArgumentException("Tag " + tag + " is not item tag!");
                    }
                    ingredient = Ingredient.of((TagKey<Item>) tag);
                }
                else {
                    throw new IllegalArgumentException("Must pass in a tag or an item!");
                }
                this.incompatibilities.add(ingredient);
            }
            return this;
        }

        public Builder setShowRequirements(boolean showRequirements) {
            this.showRequirements = showRequirements;
            return this;
        }

        public Builder setShowIncompatibilities(boolean showIncompatibilities) {
            this.showIncompatibilities = showIncompatibilities;
            return this;
        }

        public Builder setShowDescription(boolean showDescription) {
            this.showDescription = showDescription;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public CyberwareProperties build() {
            return new CyberwareProperties(requirements, incompatibilities, showRequirements, showIncompatibilities, showDescription, capacity);
        }
    }
}
