package com.vivi.cybernetics.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.vivi.cybernetics.client.shader.CybCoreShaders;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.common.util.Maath;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BlastWaveParticle extends Particle {

    private float radius;

    protected BlastWaveParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd) {
        super(pLevel, pX, pY, pZ, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = 20;
        this.radius = 8.0f;

        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.alpha = 1.0f;

        this.hasPhysics = false;
    }



    @Override
    public void render(VertexConsumer pBuffer, Camera camera, float pPartialTicks) {
        Vec3 cameraPos = camera.getPosition();


        float x = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - cameraPos.z());

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        RenderSystem.disableTexture();

        ShaderInstance shader = CybCoreShaders.getBlastWaveShader();
        if(shader == null) return;

        //uniforms
        shader.safeGetUniform("Duration").set(lifetime);
        shader.safeGetUniform("Time").set(age + pPartialTicks);

        RenderSystem.setShader(() -> shader);
        RenderHelper.resetShaderColor();


        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        Vector3f[] verticies = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0F), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};

        for(int i = 0; i < 4; i++) {
            Vector3f vertex = verticies[i];
//            vertex.transform(rotation);
            vertex.mul(radius);
            vertex.add(x, y, z);
        }

        float uMin = 0;
        float uMax = 1;
        float vMin = 0;
        float vMax = 1;
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex(verticies[0].x(), verticies[0].y(), verticies[0].z()).uv(uMin, vMin).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(verticies[1].x(), verticies[1].y(), verticies[1].z()).uv(uMin, vMax).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(verticies[2].x(), verticies[2].y(), verticies[2].z()).uv(uMax, vMax).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(verticies[3].x(), verticies[3].y(), verticies[3].z()).uv(uMax, vMin).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

//        pBuffer.vertex(verticies[0].x(), verticies[0].y(), verticies[0].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
//        pBuffer.vertex(verticies[1].x(), verticies[1].y(), verticies[1].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
//        pBuffer.vertex(verticies[2].x(), verticies[2].y(), verticies[2].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
//        pBuffer.vertex(verticies[3].x(), verticies[3].y(), verticies[3].z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();



        Tesselator.getInstance().end();
//        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }


//    @Override
    /*
    public void renderOld(VertexConsumer pBuffer, Camera camera, float pPartialTicks) {
        Vec3 cameraPos = camera.getPosition();


        float x = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - cameraPos.z());

        Quaternionf rotation;
        if (this.roll == 0.0F) {
            rotation = camera.rotation();
        } else {
            rotation = Axis.XP.rotation(camera.getXRot());
            float r = Mth.lerp(pPartialTicks, this.oRoll, this.roll);
            rotation.mul(Axis.ZP.rotation(r));
        }


        List<Vector3f> verticies = new ArrayList<>();


        float slices = 64;
        float radius = 3.0f;

        Vector3f base1 = new Vector3f(0, -0.5f, 0);
        base1.rotate(rotation);
//        base1.add(x, y, z);

        Vector3f base2 = new Vector3f(0, 0.5f, 0);
        base2.rotate(rotation);
//        base2.add(x, y, z);


        //x,z vec2
        Vector3f start = new Vector3f(-radius, 0, 0);
        for(int i = 0; i <= 3; i++) {


            float angle = -1.0f * (i/slices) * Mth.TWO_PI;
            Quaternionf yaw = Axis.YP.rotation(angle);

            Vector3f v1 = new Vector3f(base1);
            v1.add(start);
            v1.rotate(yaw);
            v1.add(x, y, z);
            Vector3f v2 = new Vector3f(base2);
            v2.add(start);
            v2.rotate(yaw);
            v2.add(x, y, z);

            verticies.add(v1);
            verticies.add(v2);
        }

        int light = this.getLightColor(pPartialTicks);

        for (int i = 0; i < verticies.size(); i++) {
            Vector3f vertex = verticies.get(i);
            pBuffer.vertex(vertex.x(), vertex.y(), vertex.z()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        }
    }
     */

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BlastWaveParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
