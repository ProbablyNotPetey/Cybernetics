package com.vivi.cybernetics.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.util.HudAnchor;
import com.vivi.cybernetics.common.ability.Ability;
import com.vivi.cybernetics.common.ability.HUDAbilityType;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AbilityHUD implements IHUDElement {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/hud/ability_hud.png");
    private final HudAnchor anchor;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final List<SubElement> elements = new ArrayList<>();
    public AbilityHUD(HudAnchor anchor, int x, int y) {
        this.anchor = anchor;
        this.x = x;
        this.y = y;
        this.width = 158;
        this.height = 26;
    }

    @Override
    public void render(PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Vec2 pos = anchor.getWithOffset(screenWidth, screenHeight, x, y);
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, 0);
        renderElenents(poseStack, partialTick);
        poseStack.popPose();
    }

    private void renderElenents(PoseStack poseStack, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int v;
        int xOffset, yOffset;
        int leftOrRight, upOrDown; // -1 = left/up, 1 = right/down
        switch (anchor) {
            case TOP_RIGHT -> {
                v = 0;
                xOffset = width - 4 - 18;
                yOffset = 4;
                leftOrRight = -1;
                upOrDown = 1;
            }
            case TOP_LEFT -> {
                v = 1;
                xOffset = 4;
                yOffset = 4;
                leftOrRight = 1;
                upOrDown = 1;
            }
            case TOP_MIDDLE -> {
                v = 2;
                xOffset = 4;
                yOffset = 4;
                leftOrRight = 1;
                upOrDown = 1;
            }
            case BOTTOM_RIGHT -> {
                v = 3;
                xOffset = width - 4 - 18;
                yOffset = height - 4 - 18;
                leftOrRight = -1;
                upOrDown = -1;
            }
            case BOTTOM_LEFT -> {
                v = 4;
                xOffset = 4;
                yOffset = height - 4 - 18;
                leftOrRight = 1;
                upOrDown = -1;
            }
            case BOTTOM_MIDDLE -> {
                v = 5;
                xOffset = 4;
                yOffset = height - 4 - 18;
                leftOrRight = 1;
                upOrDown = -1;
            }
            case MIDDLE_RIGHT -> {
                v = 6;
                xOffset = width - 4 - 18;
                yOffset = 4;
                leftOrRight = -1;
                upOrDown = 1;
            }
            case MIDDLE_LEFT -> {
                v = 7;
                xOffset = 4;
                yOffset = 4;
                leftOrRight = 1;
                upOrDown = 1;
            }
            default -> {
                v = -1;
                xOffset = 4;
                yOffset = 4;
                leftOrRight = 1;
                upOrDown = 1;
            }
        }
        if(v != -1) {
            GuiComponent.blit(poseStack, 0, 0, 0, v * 27, width, height, 256, 256);
        }

        for(int i = 0; i < elements.size(); i++) {
            poseStack.pushPose();
            poseStack.translate(xOffset + (leftOrRight * (i % 7) * 23), yOffset + (upOrDown * (i / 7) * 23), 0);
            elements.get(i).render(poseStack, partialTick);
            poseStack.popPose();
        }
    }

    @Override
    public void init() {
        updateElementList();
    }

    public void updateElementList() {
        elements.clear();
        AbilityHelper.getAbilities(Minecraft.getInstance().player).ifPresent(playerAbilities -> {
            playerAbilities.getAbilities().forEach(ability -> {
                if(ability.getType() instanceof HUDAbilityType) return;
                elements.add(new SubElement(ability));
            });
        });
    }

    @Override
    public @NotNull String getSerializedName() {
        return "ability_hud";
    }


    public static class SubElement {

        private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/hud/ability_background.png");
        private final Ability ability;
        public SubElement(Ability ability) {
            this.ability = ability;
        }

        public void render(PoseStack poseStack, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
            GuiComponent.blit(poseStack, 0, 0, 0, 0, 18, 18, 24, 24);
//            GuiComponent.blit(poseStack, 0, 0, 10, 0, 0, 18, 18, 24, 24);
        }
    }
}
