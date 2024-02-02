package com.vivi.cybernetics.datagen;

/*
public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Cybernetics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<RegistryObject<Item>> items = new HashSet<>(CybItems.ITEMS.getEntries());
        items.removeIf(i -> i.get() instanceof BlockItem);

        items.forEach(item -> {

            generatedItem(item);
            try {

            }
            catch (IllegalArgumentException e) {
                Cybernetics.LOGGER.warn("Item " + item.getKey().location() + " does not have texture provided!");
            }
        });
    }

    private void handheldItem(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        fromParent(item, "item/handheld");
    }
    private void generatedItem(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        fromParent(item, "item/generated");
    }
    private void fromParent(Item item, String parent) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        singleTexture(id.getPath(), mcLoc(parent), "layer0", modLoc("item/" + id.getPath()));
    }

    private void handheldItem(RegistryObject<Item> item) {
        ResourceLocation id = item.getKey().location();
        fromParent(item, "item/handheld");
    }
    private void generatedItem(RegistryObject<Item> item) {
        ResourceLocation id = item.getKey().location();
        fromParent(item, "item/generated");


    }
    private void fromParent(RegistryObject<Item> item, String parent) {
        ResourceLocation id = item.getKey().location();
        singleTexture(id.getPath(), mcLoc(parent), "layer0", modLoc("item/" + id.getPath()));
    }

    @Override
    public ItemModelBuilder singleTexture(String name, ResourceLocation parent, String textureKey, ResourceLocation texture) {
        existingFileHelper.trackGenerated(texture, ModelProvider.TEXTURE);
        if(existingFileHelper.exists(texture, ModelProvider.TEXTURE)) {
            return withExistingParent(name, parent)
                    .texture(textureKey, texture);
        }
        Cybernetics.LOGGER.warn("Item " + name + " does not have a texture, skipping...");
        return null;
    }
}
 */