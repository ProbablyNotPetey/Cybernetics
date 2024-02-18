#version 150

#define PI 3.1415926536f

in vec2 texCoord0;

out vec4 fragColor;

void main() {

    fragColor = vec4(texCoord0, 0, 1.0f);
}