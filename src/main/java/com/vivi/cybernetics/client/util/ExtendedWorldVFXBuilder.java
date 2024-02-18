package com.vivi.cybernetics.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class ExtendedWorldVFXBuilder extends VFXBuilders.WorldVFXBuilder {

    public static ExtendedWorldVFXBuilder createCustomWorld() {
        return new ExtendedWorldVFXBuilder();
    }


    //Uses quads instead of triangles, doesn't tile uvs.
    public VFXBuilders.WorldVFXBuilder renderQuadSphereNoTiling(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, int lats) {
        Matrix4f last = stack.last().pose();
        float startU = u0;
        float startV = v0;
        float endU = Mth.PI * 2 * u1;
        float endV = Mth.PI * v1;
        float stepU = (endU - startU) / longs;
        float stepV = (endV - startV) / lats;
        for (int i = 0; i < longs; ++i) {
            // U-points
            for (int j = 0; j < lats; ++j) {
                // V-points
                float u = i * stepU + startU;
                float v = j * stepV + startV;
                float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
                Vector3f p0 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(u, v, radius);
                Vector3f p1 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(u, vn, radius);
                Vector3f p2 = team.lodestar.lodestone.helpers.RenderHelper.parametricSphere(un, v, radius);
                Vector3f p3 = RenderHelper.parametricSphere(un, vn, radius);

                float textureU = u / endU;
                float textureV = v / endV;
                float textureUN = un / endU;
                float textureVN = vn / endV;
                supplier.placeVertex(vertexConsumer, last, p0.x(), p0.y(), p0.z(), textureU, textureV);
                supplier.placeVertex(vertexConsumer, last, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                supplier.placeVertex(vertexConsumer, last, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                supplier.placeVertex(vertexConsumer, last, p1.x(), p1.y(), p1.z(), textureU, textureVN);

                supplier.placeVertex(vertexConsumer, last, p3.x(), p3.y(), p3.z(), textureUN, textureVN);
                supplier.placeVertex(vertexConsumer, last, p1.x(), p1.y(), p1.z(), textureU, textureVN);
                supplier.placeVertex(vertexConsumer, last, p2.x(), p2.y(), p2.z(), textureUN, textureV);
                supplier.placeVertex(vertexConsumer, last, p2.x(), p2.y(), p2.z(), textureUN, textureV);
            }
        }
        return this;
    }
}
