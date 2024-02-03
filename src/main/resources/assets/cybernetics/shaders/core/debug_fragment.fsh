#version 150



uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform int Duration;
uniform float Time;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {

    fragColor = vec4(texCoord0, 0, 1.0f);
}