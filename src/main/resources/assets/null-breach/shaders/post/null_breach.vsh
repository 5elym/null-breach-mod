#version 450

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV;

layout(location = 0) out vec2 texCoord;

uniform Globals {
    mat4 OrthoMatrix;
    vec2 ScreenSize;
    float GameTime; 
};

void main() {
    gl_Position = OrthoMatrix * vec4(Position, 1.0);
    
    // Pass the UVs, but add a vertex-level fisheye/lens warp
    vec2 tc = UV;
    vec2 center = tc - 0.5;
    
    // Pulls the screen corners outward slightly for a wide-angle lens effect
    float dist = dot(center, center);
    tc += center * dist * 0.1; 
    
    texCoord = tc;
}
