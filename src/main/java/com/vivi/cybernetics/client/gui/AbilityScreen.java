package com.vivi.cybernetics.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.vivi.cybernetics.common.ability.Ability;
import com.vivi.cybernetics.common.ability.HiddenAbility;
import com.vivi.cybernetics.common.capability.PlayerAbilities;
import com.vivi.cybernetics.client.gui.util.CybAbstractWidget;
import com.vivi.cybernetics.client.gui.util.TextWidget;
import com.vivi.cybernetics.server.network.CybPackets;
import com.vivi.cybernetics.server.network.packet.c2s.C2SModifyAbilitiesPacket;
import com.vivi.cybernetics.common.registry.CybAbilities;
import com.vivi.cybernetics.common.util.AbilityHelper;
import com.vivi.cybernetics.common.util.Maath;
import com.vivi.cybernetics.client.util.Easing;
import com.vivi.cybernetics.client.util.InputHelper;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.client.util.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

import java.util.List;


//todo: major refactor and cleanup. fix math
public class AbilityScreen extends Screen {

    public static final int SLICES = 128;
    private float centerX;
    private float centerY;
    private long time;

    private Player player;
    private TextWidget textWidget;
    public AbilityScreen() {
        super(Component.literal("Abilities"));
    }

    @Override
    protected void init() {
        super.init();
        time = 0L;
        player = Minecraft.getInstance().player;
        centerX = minecraft.getWindow().getGuiScaledWidth() / 2.0f;
        centerY = minecraft.getWindow().getGuiScaledHeight() / 2.0f;

        PlayerAbilities abilities = AbilityHelper.getAbilities(player).orElse(null);
        if(abilities == null) return;
        List<Ability> filtered = abilities.getAbilities().stream().filter(ability -> !ability.getType().getClass().isAnnotationPresent(HiddenAbility.class)).toList();
        int sections = filtered.size();
        float length = 360.0f / sections;
        for(int i = 0; i < sections; i++) {
            addRenderableWidget(new AbilitySlice(filtered.get(i), 60, 100, length * i, length));
        }
        textWidget = new TextWidget(this, (int) centerX, (int) centerY);
        textWidget.setY(textWidget.getY() - textWidget.getTextHeight() / 2);
        addRenderableWidget(textWidget);
        textWidget.setText(Component.literal("Abilities"));
    }

    @Override
    public void tick() {
        time++;
        textWidget.tick(time);
        textWidget.setX((int) centerX - (textWidget.getTextWidth() / 2));
        this.renderables.forEach(widget -> {
            if(widget instanceof AbilitySlice slice) {
                slice.setSelected(slice.isHoveredOrFocused() && slice.getAbility().getCooldown() == -1);
            }
        });

//        Cybernetics.LOGGER.info("Key down: " + CybKeybinds.PLAYER_CYBERWARE_MENU.isDown());
        if(!InputHelper.isAbilityKeyHeld()) {
            minecraft.setScreen(null);
        }
//        if(!CybKeybinds.PLAYER_CYBERWARE_MENU.isDown()) {
//            minecraft.setScreen(null);
//        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
//        RenderSystem.disableTexture();
        RenderHelper.resetShaderColor();

//        float length = 60;
//        for(int i = 0; i < 6; i++) {
//            RenderSystem.setShaderColor((i + 1) / 6.0f, 0.0f, 0.0f, 1.0f);
//            drawAnnulus(poseStack, 40 + (4 * i), 80 + (4 * i), length * i, length * (i+1));
//        }

//        drawTorus(poseStack, 40, 80, 60, 120);

        RenderSystem.setShaderColor(0.45f, 0.05f, 0.05f, 0.75f);
        drawAnnulus(guiGraphics.pose(), 53, 55);


//        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public void updateText(Component text) {
        textWidget.setText(text, true);
        textWidget.setX((int) centerX - (textWidget.getTextWidth() / 2));
    }

    private void drawAnnulus(PoseStack poseStack, float innerRadius, float outerRadius) {
        drawAnnulus(poseStack, innerRadius, outerRadius, 0, 360);
    }
    private void drawAnnulus(PoseStack poseStack, float innerRadius, float outerRadius, float startAngle, float stopAngle) {



        float totalAngle = stopAngle - startAngle;

        //percent of total slices to use
        float slices = SLICES * (totalAngle / 360.0F);

        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0.0f);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tesselator.getBuilder();

        vertexBuffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION);
        Matrix4f matrix4f = poseStack.last().pose();

        float angle = Mth.DEG_TO_RAD * (totalAngle / slices);
        float startRad = Mth.DEG_TO_RAD * startAngle;
        for(int i = 0; i <= slices; i++) {

            float offset = i;
            if(i + 1 > slices) {
                offset = slices;
            }

            float cos = Mth.cos(-(startRad + angle * offset));
            float sin = Mth.sin(-(startRad + angle * offset));
            vertexBuffer.vertex(matrix4f, innerRadius * cos, innerRadius * sin, 0).endVertex();
            vertexBuffer.vertex(matrix4f, outerRadius * cos, outerRadius * sin, 0).endVertex();
        }



        //SHAPES DRAW IN COUNTER CLOCKWISE MOTION!

//        vertexBuffer.vertex(matrix4f, 1.0f, 200.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 100.0f, 200.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 100.0f, 1.0f, 0.0f).endVertex();
//        vertexBuffer.vertex(matrix4f, 1.0f, 1.0f, 0.0f).endVertex();

        tesselator.end();


        poseStack.popPose();
    }

    @Override
    public void removed() {
        this.renderables.forEach(widget -> {
            if(widget instanceof AbilitySlice slice && slice.isSelected()) {
                C2SModifyAbilitiesPacket.OpCode opCode;
                if(slice.getAbility().isEnabled()) {
                    opCode = C2SModifyAbilitiesPacket.OpCode.DISABLE;
                    AbilityHelper.disableAbility(player, slice.getAbility().getType());
                }
                else {
                    opCode = C2SModifyAbilitiesPacket.OpCode.ENABLE;
                    AbilityHelper.enableAbility(player, slice.getAbility().getType());
                }
                CybPackets.sendToServer(new C2SModifyAbilitiesPacket(slice.getAbility().getType(), opCode));
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public class AbilitySlice extends CybAbstractWidget {

        private boolean selected;
        private float inner;
        private float outer;
        private float startAngle;
        private float totalAngle;
        private final Ability ability;
        public AbilitySlice(Ability ability, float inner, float outer, float startAngle, float totalAngle) {
            super((int) centerX, (int) centerY, 1, 1, Component.empty());
            this.playSound = false;
            this.inner = inner;
            this.outer = outer;
            this.startAngle = startAngle;
            this.totalAngle = totalAngle;
            this.alpha = 0.75f;
            this.ability = ability;
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
        @Override
        public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            if(!this.visible) return;
            //converts mouse points to polar
            float mouseR = Maath.toRadius(pMouseX - centerX, -(pMouseY - centerY));
            float mouseT = Mth.RAD_TO_DEG * Maath.toAngle(pMouseX - centerX, -(pMouseY - centerY));

            this.isHovered = (mouseR >= 55) && (mouseT >= startAngle) && (mouseT < (startAngle + totalAngle));
            renderWidget(guiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
//            RenderSystem.disableTexture();
            RenderHelper.resetShaderColor();

            if(ability.isEnabled()) {
                RenderSystem.setShaderColor(0.0f, 1.0f, 0.968f, alpha);
            }
            else {
                RenderSystem.setShaderColor(0.45f, 0.05f, 0.05f, alpha);
            }
            drawAnnulus(guiGraphics.pose(), inner, outer, startAngle, startAngle + totalAngle);

            if(ability.getCooldown() > 0) {
                RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, alpha);
                drawAnnulus(guiGraphics.pose(), inner, outer, startAngle + ((totalAngle / 2) * (1 - ((float) ability.getCooldown() / ability.getType().getMaxCooldown()))), startAngle + totalAngle - ((totalAngle / 2) * (1 - ((float) ability.getCooldown() / ability.getType().getMaxCooldown()))));
            }

//            RenderSystem.enableTexture();
            RenderSystem.disableBlend();

            //render item
            ResourceLocation texture = ability.getType().getTexture();
            if(texture != null) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                float centerAngle = Mth.DEG_TO_RAD * (startAngle + (totalAngle / 2));
                float centerRadius = inner + (outer - inner) / 2;
                float scale = 2.0f;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(scale, scale, 1.0f);
                guiGraphics.pose().translate((Maath.toX(centerRadius, centerAngle) + centerX) / scale, (-Maath.toY(centerRadius, centerAngle) + centerY) / scale, 0.0);
                guiGraphics.blit(texture, -8, -8, 200, 0, 0, 16, 16, 16, 16);
                guiGraphics.pose().popPose();
                RenderSystem.applyModelViewMatrix();

            }
        }

        public void setSelected(boolean selected) {
            if(this.selected != selected) {
                if(selected) {
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getInner, this::setInner, 70, 7, Easing.CIRC_OUT);
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getOuter, this::setOuter, 110, 7, Easing.CIRC_OUT);
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getAlpha, this::setAlpha, 0.9f, 7, Easing.CIRC_OUT);
                    ResourceLocation abilityRLoc = CybAbilities.ABILITY_TYPE_REGISTRY.get().getKey(ability.getType());
                    AbilityScreen.this.updateText(Component.translatable("tooltip." + abilityRLoc.getNamespace() + ".ability." + abilityRLoc.getPath()));
                }
                else {
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getInner, this::setInner, 60, 10, Easing.QUAD_OUT);
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getOuter, this::setOuter, 100, 10, Easing.QUAD_OUT);
                    ScreenHelper.addAnimation(AbilityScreen.this, this::getAlpha, this::setAlpha, 0.75f, 7, Easing.CIRC_OUT);
                }
            }
            this.selected = selected;
        }

        public boolean isSelected() {
            return selected;
        }

        public float getInner() {
            return inner;
        }

        public float getOuter() {
            return outer;
        }
        public float getAlpha() {
            return alpha;
        }

        public void setInner(float inner) {
            this.inner = inner;
        }

        public void setOuter(float outer) {
            this.outer = outer;
        }

        public Ability getAbility() {
            return ability;
        }
    }
}
