#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform vec3 Center;
uniform mat4 InvViewMat;

out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord2;
out vec3 normals;
out vec3 worldCoord;

void main() {
    vec4 pos = ProjMat * ModelViewMat * vec4(Position, 1.0);
    gl_Position = pos;


    vertexColor = Color;
    texCoord0 = UV0;
    texCoord2 = UV2;
    worldCoord = (InvViewMat * (ModelViewMat * vec4(Position, 1.0))).xyz;
    normals = normalize(worldCoord - Center);
//    normals = worldCoord.xyz;
}