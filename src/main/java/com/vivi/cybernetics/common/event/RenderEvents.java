package com.vivi.cybernetics.common.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.shader.ScannerRenderer;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class RenderEvents {


    @SubscribeEvent
    public static void onRenderLevelEvent(RenderLevelStageEvent event) {
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            ScannerRenderer.renderScan(event.getPoseStack(), event.getProjectionMatrix(), event.getRenderTick(), event.getPartialTick(), event.getCamera(), event.getFrustum());
        }
    }
}
