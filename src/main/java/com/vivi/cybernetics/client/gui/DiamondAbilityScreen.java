package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.common.ability.AbilityType;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

@Deprecated
public class DiamondAbilityScreen extends Screen {

    private static final int radius = 41;
    private Player player;

    private int centerX;
    private int centerY;
    public DiamondAbilityScreen() {
        super(Component.literal("Abilities"));
    }


    @Override
    protected void init() {
        super.init();
        player = minecraft.player;
        centerX = minecraft.getWindow().getGuiScaledWidth() / 2;
        centerY = minecraft.getWindow().getGuiScaledHeight() / 2;

        PlayerAbilities abilities = AbilityHelper.getAbilities(player).orElse(null);
        if(abilities == null) return;
        int id = 0;
        addRenderableWidget(new AbilityWidget(centerX, centerY - radius, id++, null));
        addRenderableWidget(new AbilityWidget(centerX + radius / 2, centerY - radius / 2, id++, null));
        addRenderableWidget(new AbilityWidget(centerX + radius, centerY, id++, null));
        addRenderableWidget(new AbilityWidget(centerX + radius / 2, centerY + radius / 2, id++, null));
        addRenderableWidget(new AbilityWidget(centerX, centerY + radius, id++, null));
        addRenderableWidget(new AbilityWidget(centerX - radius / 2, centerY + radius / 2, id++, null));
        addRenderableWidget(new AbilityWidget(centerX - radius, centerY, id++, null));
        addRenderableWidget(new AbilityWidget(centerX - radius / 2, centerY - radius / 2, id++, null));

    }


    public static class AbilityWidget extends CybAbstractWidget {


        private static final ResourceLocation ACTIVE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/ability/active.png");
        private static final ResourceLocation ACTIVE_HOVER = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/ability/active_hover.png");

        private final int id;
        public AbilityWidget(int pX, int pY, int id, AbilityType type) {
            super(pX, pY, 49, 49, Component.empty());
            this.playSound = false;
            this.id = id;
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

            RenderSystem.setShaderTexture(0, ACTIVE);
            RenderHelper.resetShaderColor();

            int texturePos = switch (id) {
                case 0 -> 5;
                case 1 -> 2;
                case 2 -> 0;
                case 3 -> 7;
                case 4 -> 4;
                case 5 -> 6;
                case 6 -> 1;
                case 7 -> 3;
                default -> throw new IllegalArgumentException("Id " + id + "must be between 0 and 7!");
            };
            int u = (texturePos % 4) * 50;
            int v = (texturePos / 4) * 50;
            pPoseStack.pushPose();
            pPoseStack.translate(-1.0 * width / 2, -1.0 * height / 2, 0.0);
            blit(pPoseStack, x, y, u, v, width, height, 200, 100);
            pPoseStack.popPose();
        }

        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
}
