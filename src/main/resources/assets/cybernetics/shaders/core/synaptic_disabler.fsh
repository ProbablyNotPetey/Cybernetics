#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

uniform vec3 CameraPos;
uniform int Inverted;


in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

in vec3 normals;
in vec3 worldCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a < 0.05) {
        discard;
    }
//    fragColor = color * ColorModulator;

//    fragColor = vec4(normals, 1.0);


    vec4 blue = vec4(0.216,0.957,0.957, color.a);

    vec3 viewVector = normalize(CameraPos - worldCoord);
    vec3 newNormals = normals;
    if(Inverted == 1) {
        newNormals *= -1;
    }
    float fresnel = 1 - dot(viewVector, newNormals);

    fragColor = fresnel * blue;
//    fragColor = vertexColor;
//    fragColor = vec4(Inverted, Inverted, Inverted, 1);
}