#version 150

#moj_import <fog.glsl>
#moj_import <cybernetics:easings.glsl>
#moj_import <cybernetics:fast_noise_lite.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

uniform int Duration;
uniform float Time;
uniform int VertexOffset;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

float threshold = 0.25;

void main() {

    //particle.vsh

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    texCoord0 = UV0;
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);


    //vertex offsets
    if(VertexOffset == 0) {
        return;
    }

    fnl_state noise = fnlCreateState(69);
    noise.noise_type = FNL_NOISE_PERLIN;

    float percent = Time / Duration;
    float movePercent = EaseOutCubic(percent);

    vec2 ndc = texCoord0 * 2 - 1;
    float dist = length(ndc);
    float f = movePercent - dist;
    if((f < 0 || f > threshold)) {
        return;
    }

    vec2 noiseCoord = texCoord0 * vec2(1920.0f, 1080.0f);

    float offset = smoothstep(0, 1, 1 - (f / threshold));
    offset *= (fnlGetNoise3D(noise, noiseCoord.x + Time * 30.0f, Time * 30.0f, noiseCoord.y) / 2.0f + 0.5f);

    float alphaPercent = 1 - EaseInQuint(percent);
    offset *= alphaPercent;

    if(offset < 0.05) {
        return;
    }

    gl_Position.y += offset * 0.35f;

}