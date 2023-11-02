#version 150

uniform sampler2D DepthBuffer;

in vec2 texCoord0;
out vec4 fragColor;

void main() {
    float depth = texture2D(DepthBuffer, texCoord0).r;

    vec4 color = vec4(0, 0, 0, 0);
    if(depth < 1) {
        color = vec4(depth, 0, 0, 1.0);
    }

    fragColor = color;
}
