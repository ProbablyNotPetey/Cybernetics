package com.vivi.cybernetics.client.hud;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.util.HudAnchor;
import com.vivi.cybernetics.client.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Replaces the vanilla potion effects so I don't have to think about it lol
 */
public class MobEffectHUD implements IHUDElement {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/hud/mob_effect_hud.png");
    private final HudAnchor anchor;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private int u;
    private int xOffset, yOffset;
    private int leftOrRight, upOrDown; // -1 = left/up, 1 = right/down

    public MobEffectHUD(HudAnchor anchor, int x, int y) {
        this.anchor = anchor;
        this.x = x;
        this.y = y;
        this.width = 26;
        this.height = 158;
    }

    @Override
    public void init() {
        switch (anchor) {
            case TOP_LEFT, MIDDLE_LEFT -> {
                u = 1;
                leftOrRight = 1;
                upOrDown = 1;
                xOffset = 5;
                yOffset = 0;
            }
            case TOP_MIDDLE, MIDDLE -> {
                u = -1;
                leftOrRight = 1;
                upOrDown = 1;
                xOffset = 5;
                yOffset = 0;
            }
            case TOP_RIGHT, MIDDLE_RIGHT -> {
                u = 0;
                leftOrRight = -1;
                upOrDown = 1;
                xOffset = 3;
                yOffset = 0;
            }
            case BOTTOM_LEFT -> {
                u = 1;
                leftOrRight = 1;
                upOrDown = -1;
                xOffset = 5;
                yOffset = height - 18;
            }
            case BOTTOM_MIDDLE -> {
                u = -1;
                leftOrRight = 1;
                upOrDown = -1;
                xOffset = 5;
                yOffset = height - 18;
            }
            case BOTTOM_RIGHT -> {
                u = 0;
                leftOrRight = -1;
                upOrDown = -1;
                xOffset = 3;
                yOffset = height - 18;
            }
        }
    }



    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Vec2 pos = anchor.getWithOffset(screenWidth, screenHeight, this.x, this.y);
        renderEffects(gui, poseStack, pos);

    }

    @Override
    public String getSerializedName() {
        return "mob_effect_hud";
    }

    public void renderEffects(ForgeGui gui, PoseStack poseStack, Vec2 pos) {
        List<MobEffectInstance> effectInstances = Minecraft.getInstance().player.getActiveEffects().stream().filter(MobEffectInstance::showIcon).toList();
        if(effectInstances.isEmpty()) return;


        RenderSystem.setShaderTexture(0, TEXTURE);
        if(u != -1) {
            GuiComponent.blit(poseStack, (int)pos.x, (int)pos.y, 0, u * 27, 0, width, height, 160,160);
        }


        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        MobEffectTextureManager mobEffectTextures = Minecraft.getInstance().getMobEffectTextures();
        int numEffects = 0;

        //I dont know why these are all runnables but I'm just gonna go with it
        List<Runnable> runnables = Lists.newArrayListWithExpectedSize(effectInstances.size());


        for(MobEffectInstance effectInstance : Ordering.natural().reverse().sortedCopy(effectInstances)) {
            IClientMobEffectExtensions renderer = IClientMobEffectExtensions.of(effectInstance);
            if (!renderer.isVisibleInGui(effectInstance)) continue;


            MobEffect effect = effectInstance.getEffect();

            int xPos = (int)pos.x + xOffset;
            int yPos = (int)pos.y + yOffset;


            yPos += (upOrDown) * 23 * (numEffects % 7);
            xPos += (leftOrRight) * 23 * (numEffects / 7);
            numEffects++;


            // BACKGROUND

            RenderHelper.resetShaderColor();
            RenderSystem.setShaderTexture(0, AbilityHUD.SubElement.BACKGROUND_TEXTURE);
            GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 18, 18, 48, 48);


            // ICONS

            RenderHelper.resetShaderColor();
            float alpha = 1.0f;

            if(!effectInstance.isAmbient()) {
                if (effectInstance.getDuration() <= 200) {
                    int k = 10 - effectInstance.getDuration() / 20;
                    alpha = Mth.clamp((float)effectInstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float)effectInstance.getDuration() * (float)Math.PI / 5.0F) * Mth.clamp((float)k / 10.0F * 0.25F, 0.0F, 0.25F);
                }
            }

            //custom effect rendering
            if (renderer.renderGuiIcon(effectInstance, gui, poseStack, xPos, yPos, 0, alpha)) continue;

            //normal sprite rendering
            TextureAtlasSprite textureatlassprite = mobEffectTextures.get(effect);
            int xPosFinal = xPos;
            int yPosFinal = yPos;
            float alphaFinal = alpha;
            runnables.add(() -> {
                RenderSystem.setShaderTexture(0, textureatlassprite.atlas().location());
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaFinal);
                GuiComponent.blit(poseStack, xPosFinal + 3, yPosFinal + 3, 10, 12, 12, textureatlassprite);
            });



        }

        runnables.forEach(Runnable::run);
        RenderHelper.resetShaderColor();
    }





    /**
     * Vanilla mob effect rendering, with some non-necessary bits removed. Also all local variables named.
     */
    public void renderEffectsVanilla(ForgeGui gui, PoseStack poseStack, int screenWidth, int screenHeight) {
        Collection<MobEffectInstance> effectInstances = Minecraft.getInstance().player.getActiveEffects();
        if (!effectInstances.isEmpty()) {

            RenderSystem.enableBlend();
            int beneficialEffeccts = 0;
            int harmfulEffects = 0;
            MobEffectTextureManager mobEffectTextures = Minecraft.getInstance().getMobEffectTextures();
            List<Runnable> runnables = Lists.newArrayListWithExpectedSize(effectInstances.size());
            RenderSystem.setShaderTexture(0, AbstractContainerScreen.INVENTORY_LOCATION);

            for(MobEffectInstance effectInstance : Ordering.natural().reverse().sortedCopy(effectInstances)) {
                MobEffect effect = effectInstance.getEffect();

                //custom mob effect rendering
                IClientMobEffectExtensions renderer = IClientMobEffectExtensions.of(effectInstance);
                if (!renderer.isVisibleInGui(effectInstance)) continue;


                // Rebind in case previous renderHUDEffect changed texture
                RenderSystem.setShaderTexture(0, AbstractContainerScreen.INVENTORY_LOCATION);

                //render icon
                if (effectInstance.showIcon()) {

                    int xPos = screenWidth;
                    int yPos = 1;


                    if (effect.isBeneficial()) {
                        ++beneficialEffeccts;
                        xPos -= 25 * beneficialEffeccts;
                    } else {
                        ++harmfulEffects;
                        xPos -= 25 * harmfulEffects;
                        yPos += 26;
                    }

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                    float opacity = 1.0F;

                    if (effectInstance.isAmbient()) {
                        GuiComponent.blit(poseStack, xPos, yPos, 0, 165, 166, 24, 24, 256, 256);
                    } else {
                        GuiComponent.blit(poseStack, xPos, yPos, 0, 141, 166, 24, 24, 256, 256);
                        if (effectInstance.getDuration() <= 200) {
                            int k = 10 - effectInstance.getDuration() / 20;
                            opacity = Mth.clamp((float)effectInstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float)effectInstance.getDuration() * (float)Math.PI / 5.0F) * Mth.clamp((float)k / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }

                    //custom effect rendering
                    if (renderer.renderGuiIcon(effectInstance, gui, poseStack, xPos, yPos, 0, opacity)) continue;

                    //normal sprite rendering
                    TextureAtlasSprite textureatlassprite = mobEffectTextures.get(effect);
                    int xPosFinal = xPos;
                    int yPosFinal = yPos;
                    float opacityFinal = opacity;
                    runnables.add(() -> {
                        RenderSystem.setShaderTexture(0, textureatlassprite.atlas().location());
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacityFinal);
                        GuiComponent.blit(poseStack, xPosFinal + 3, yPosFinal + 3, 0, 18, 18, textureatlassprite);
                    });
                }
            }

            runnables.forEach(Runnable::run);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }


}
