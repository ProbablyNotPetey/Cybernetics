package com.vivi.cybernetics.client.shader;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;

public class BerserkRenderer {
    private static final BerserkRenderer INSTANCE = new BerserkRenderer();
    private byte renderPhase = -1;
    private int startDuration;
    private Easing inEasing;
    private int stopDuration;
    private Easing outEasing;
    private int startTicks = -1;

    private BerserkRenderer() {
    }

    public void start(int duration, Easing easing) {
        //intro
        renderPhase = 1;
        this.startDuration = duration;
        this.inEasing = easing;
        this.startTicks = -1;
    }

    public void stop(int duration, Easing easing) {
        //outro
        renderPhase = 2;
        this.stopDuration = duration;
        this.outEasing = easing;
        this.startTicks = -1;
    }



    public static BerserkRenderer getInstance() {
        return INSTANCE;
    }

    public void render(PoseStack poseStack, int ticks, float partialTick) {
        if(renderPhase == -1) return;
        if(startTicks == -1) startTicks = ticks;

        float progress = 1.0f;

        if(renderPhase == 1) {
            float normalizedTime = (ticks - startTicks) + partialTick;
            float percent = normalizedTime / startDuration;
            if(percent >= 1.0f) {
                renderPhase = 0;
            }
            else {
                progress = inEasing.ease(percent);
            }
        }
        else if(renderPhase == 2) {
            float normalizedTime = (ticks - startTicks) + partialTick;
            float percent = normalizedTime / startDuration;
            if(percent >= 1.0f) {
                renderPhase = -1;
                progress = 0.0f;
            }
            else {
                progress = 1 - outEasing.ease(percent);
            }
        }

        PostChain postChain = CybPostShaders.getInstance().getBerserk();
        if(postChain == null) return;
        postChain.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
        for (PostPass pass : postChain.passes) {
            EffectInstance shader = pass.getEffect();
            shader.safeGetUniform("Progress").set(progress);
        }
        postChain.process(partialTick);

    }
}
