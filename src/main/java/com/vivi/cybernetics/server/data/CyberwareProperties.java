package com.vivi.cybernetics.server.data;

import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyberwareProperties {

    private final List<Ingredient> requirements;
    private final List<Ingredient> incompatibilities;
    private final boolean showRequirements;
    private final boolean showIncompatibilities;
    private final boolean showDescription;
    private final int capacity;


    public CyberwareProperties(List<Ingredient> requirements, List<Ingredient> incompatibilities, boolean showRequirements, boolean showIncompatibilities, boolean showDescription, int capacity) {
        this.requirements = requirements;
        this.incompatibilities = incompatibilities;
        this.showRequirements = showRequirements;
        this.showIncompatibilities = showIncompatibilities;
        this.showDescription = showDescription;
        this.capacity = capacity;
    }

    public static CyberwareProperties fromStringList(List<String> requirements, List<String> incompatibilities, boolean showRequirements, boolean showIncompatibilities, boolean showDescription, int capacity) {
        return new CyberwareProperties(toIngredientList(requirements), toIngredientList(incompatibilities), showRequirements, showIncompatibilities, showDescription, capacity);
    }
    public static List<Ingredient> toIngredientList(List<String> stringList) {
        List<Ingredient> output = new ArrayList<>();
        stringList.forEach(str -> {
            if(str.startsWith("#")) {
                ResourceLocation rLoc = ResourceLocation.tryParse(str.substring(1));
                if(rLoc == null) {
                    throw new ResourceLocationException("Tag " + str + "is not a valid resource location!");
                }
                output.add(Ingredient.of(ItemTags.create(rLoc)));
            }
            else {
                ResourceLocation rLoc = ResourceLocation.tryParse(str);
                if(rLoc == null) {
                    throw new ResourceLocationException("Tag " + str + "is not a valid resource location!");
                }
                output.add(Ingredient.of(ForgeRegistries.ITEMS.getValue(rLoc)));
            }
        });
        return output;
    }

    public void toNetwork(FriendlyByteBuf buf) {
//        buf.writeVarInt(REQUIREMENTS_LIST);
        buf.writeCollection(requirements, (buf1, ingredient) -> {
            ingredient.toNetwork(buf1);
        });
//        buf.writeVarInt(INCOMPATIBILITIES_LIST);
        buf.writeCollection(incompatibilities, (buf1, ingredient) -> {
            ingredient.toNetwork(buf1);
        });
        buf.writeBoolean(showRequirements);
        buf.writeBoolean(showIncompatibilities);
        buf.writeBoolean(showDescription);
        buf.writeVarInt(capacity);
    }

    public static CyberwareProperties fromNetwork(FriendlyByteBuf buf) {
        List<Ingredient> req = buf.readList(Ingredient::fromNetwork);
        List<Ingredient> inc = buf.readList(Ingredient::fromNetwork);
        boolean showReq = buf.readBoolean();
        boolean showInc = buf.readBoolean();
        boolean showDesc = buf.readBoolean();
        int cap = buf.readVarInt();
        return new CyberwareProperties(req, inc, showReq, showInc, showDesc, cap);
    }

    @Override
    public String toString() {
        return "Requirements: {" + listToString(requirements) + "}, Incompatibilities: {" + listToString(incompatibilities) + "}";
    }

    private String listToString(List<Ingredient> ingredients) {
        StringBuilder output = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            output.append(Arrays.toString(ingredient.getItems()));
        }
        return output.toString();
    }

    public List<Ingredient> getRequirements() {
        return requirements;
    }

    public List<Ingredient> getIncompatibilities() {
        return incompatibilities;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean showRequirements() {
        return showRequirements;
    }

    public boolean showIncompatibilities() {
        return showIncompatibilities;
    }

    public boolean showDescription() {
        return showDescription;
    }
}
