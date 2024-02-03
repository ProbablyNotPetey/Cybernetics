package com.vivi.cybernetics.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.vivi.cybernetics.client.shader.CybCoreShaders;
import com.vivi.cybernetics.client.util.CustomWorldParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.particle.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.GenericParticle;


public class FallingParticle extends GenericParticle<WorldParticleOptions> {

    //todo: switch to this, have to AT :sob:
//    public static final RenderType FALLING_PARTICLE_RENDER_TYPE = createGenericRenderType("cybernetics:falling_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, LodestoneRenderTypeRegistry.builder()
//            .setShaderState(LodestoneShaderRegistry.PARTICLE)
//            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
//            .setTextureState(RenderStateShard.TextureStateShard.NO_TEXTURE)
//            .setCullState(RenderStateShard.NO_CULL));
    public static final LodestoneWorldParticleRenderType FALLING_PARTICLE = new CustomWorldParticleRenderType(
            LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE, CybCoreShaders::getFallingParticleShader, null,
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public FallingParticle(ClientLevel world, WorldParticleOptions options, double x, double y, double z, double xd, double yd, double zd) {
        super(world, options, null, x, y, z, xd, yd, zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (lifeDelay > 0) {
            return;
        }
        renderActors.forEach(actor -> actor.accept(this));
        renderParticle(consumer, camera, partialTicks);
    }

    private void renderParticle(VertexConsumer consumer, Camera camera, float partialTicks) {

//        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

//        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
//        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);


//        RenderSystem.setShader(CybCoreShaders::getFallingParticleShader);
//        RenderHelper.resetShaderColor();


        Vec3 vec3 = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        float yaw = -camera.getYRot();

        float height = 3.0f + 2 * (age + partialTicks);

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-0.125F, -height, 0.0F), new Vector3f(-0.125F, height, 0.0F), new Vector3f(0.125F, height, 0.0F), new Vector3f(0.125F, -height, 0.0F)};
        float f3 = this.getQuadSize(partialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(Axis.YP.rotationDegrees(yaw));
            vector3f.mul(f3);
            vector3f.add(x, y, z);
        }

        float uMin = 0;
        float uMax = 1;
        float vMin = 0;
        float vMax = 1;
        int j = this.getLightColor(partialTicks);
        consumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(uMax, vMax).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(uMax, vMin).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(uMin, vMin).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(uMin, vMax).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

//        Tesselator.getInstance().end();

//        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
    }
}
