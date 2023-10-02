package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModTags;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if(!stack.isEmpty() && stack.getItem() instanceof CyberwareItem) {

            MutableComponent sectionTooltip = Component.translatable("tooltip.cybernetics.cyberware").append(": ");

            if(stack.is(ModTags.ANY_SECTION)) {
                sectionTooltip.append(Component.translatable("tooltip.cybernetics.section.any"));
            }
            else {
                List<CyberwareSectionType> sections = CyberwareHelper.getValidCyberwareSections(stack);
                for (int i = 0; i < sections.size(); i++) {
                    ResourceLocation id = ModCyberware.CYBERWARE_SECTION_TYPE_REGISTRY.get().getKey(sections.get(i));
                    sectionTooltip.append(Component.translatable("tooltip." + id.getNamespace() + ".section." + id.getPath()));
                    if(i < sections.size() - 1) sectionTooltip.append(", ");
                }
            }

            sectionTooltip = sectionTooltip.withStyle(ChatFormatting.RED);
            event.getToolTip().add(sectionTooltip);
        }
    }
}
