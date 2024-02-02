package com.vivi.cybernetics.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.vivi.cybernetics.client.shader.CybCoreShaders;
import com.vivi.cybernetics.client.util.RenderHelper;
import com.vivi.cybernetics.common.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BlastWaveParticle extends Particle {

    private float radius;

    protected BlastWaveParticle(BlastWaveParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd) {
        super(pLevel, pX, pY, pZ, xd, yd, zd);

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = options.duration;
        this.radius = options.radius;

        this.alpha = (options.argb >> 24 & 255) / 255.0f;
        this.rCol = (options.argb >> 16 & 255) / 255.0f;
        this.gCol = (options.argb >> 8 & 255) / 255.0f;
        this.bCol = (options.argb & 255) / 255.0f;

        this.hasPhysics = false;
    }


    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if(ClientConfig.simplifyShockwave.get()) {
            renderFlat(pBuffer, pRenderInfo, pPartialTicks);
        }
        else {
            renderTessellated(pBuffer, pRenderInfo, pPartialTicks);
        }
    }

    public void renderTessellated(VertexConsumer pBuffer, Camera camera, float pPartialTicks) {
        Vec3 cameraPos = camera.getPosition();

        float x = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - cameraPos.x()) - radius;
        float y = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - cameraPos.z()) - radius;

        RenderSystem.enableBlend();
//        RenderSystem.disableCull();
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

        int scale = 64; //how many quads per row/column

        List<Vector3f> verticies = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();

        float quadSize = radius * 2.0f / scale;
        float uvSize = 1.0f/scale;
        for(int r = 0; r < scale; r++) {
            for(int c = 0; c < scale; c++) {
                verticies.add(new Vector3f(0, 0, 0).add(r * quadSize, 0, c * quadSize).add(x, y, z));
                verticies.add(new Vector3f(0, 0, quadSize).add(r * quadSize, 0, c * quadSize).add(x, y, z));
                verticies.add(new Vector3f(quadSize, 0, quadSize).add(r * quadSize, 0, c * quadSize).add(x, y, z));
                verticies.add(new Vector3f(quadSize, 0, 0).add(r * quadSize, 0, c * quadSize).add(x, y, z));

                uvs.add(new Vector2f(0, 0).add(r * uvSize, c * uvSize));
                uvs.add(new Vector2f(0, uvSize).add(r * uvSize, c * uvSize));
                uvs.add(new Vector2f(uvSize, uvSize).add(r * uvSize, c * uvSize));
                uvs.add(new Vector2f(uvSize, 0).add(r * uvSize, c * uvSize));

            }
        }

        int j = this.getLightColor(pPartialTicks);

        for(int i = 0; i < verticies.size(); i++) {
            Vector3f vertex = verticies.get(i);
            Vector2f uv = uvs.get(i);
            pBuffer.vertex(vertex.x(), vertex.y(), vertex.z()).uv(uv.x(), uv.y()).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        }

        Tesselator.getInstance().end();
//        RenderSystem.enableTexture();
//        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void renderFlat(VertexConsumer pBuffer, Camera camera, float pPartialTicks) {
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
        shader.safeGetUniform("VertexOffset").set(0); //disables vertex offsetting, as only using 1 quad

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

    public static class Provider implements ParticleProvider<BlastWaveParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(BlastWaveParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BlastWaveParticle(options, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
