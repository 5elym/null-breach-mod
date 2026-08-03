#version 450

// Vulkan requires explicit memory locations for inputs and outputs
layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

layout(binding = 0) uniform Globals {
    ivec3 CameraBlockPos;   // 12 bytes
    vec3 CameraOffset;      // 12 bytes
    vec2 ScreenSize;        // 8 bytes
    float GlintAlpha;       // 4 bytes
    float GameTime;         // 4 bytes <--- MOVED TO THIS EXACT POSITION
    int MenuBlurRadius;     // 4 bytes
    int UseRgss;            // 4 bytes
};

// 3. Texture sampler assigned to the next contiguous binding slot
uniform sampler2D DiffuseSampler;

// --- Pure Procedural Noise ---
float proceduralNoise(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

// --- Procedural Sine-Wave Distortion Field ---
vec2 proceduralWarp(vec2 uv) {
    vec2 p = uv * 8.0;
    float wave1 = sin(p.x + p.y) * 0.02;
    float wave2 = cos(p.x - p.y) * 0.02;
    return uv + vec2(wave1, wave2);
}

void main() {
    // 1. Apply procedural spatial warping to coordinates
    vec2 warpedCoord = proceduralWarp(texCoord);

    // 2. Sample the game world through the warped coordinate space
    vec4 centerCol = texture(DiffuseSampler, warpedCoord);

    // 3. Mathematical Edge Detection (Wireframe/Outline logic)
    float offset = 0.0025;
    float up    = dot(texture(DiffuseSampler, warpedCoord + vec2(0.0, offset)).rgb,    vec3(0.299, 0.587, 0.114));
    float down  = dot(texture(DiffuseSampler, warpedCoord - vec2(0.0, offset)).rgb,    vec3(0.299, 0.587, 0.114));
    float left  = dot(texture(DiffuseSampler, warpedCoord - vec2(offset, 0.0)).rgb,    vec3(0.299, 0.587, 0.114));
    float right = dot(texture(DiffuseSampler, warpedCoord + vec2(offset, 0.0)).rgb,    vec3(0.299, 0.587, 0.114));
    
    float edgeFactor = abs(up - down) + abs(right - left);
    edgeFactor = smoothstep(0.04, 0.25, edgeFactor); 
    
    vec3 redOutline = vec3(0.85, 0.05, 0.1) * edgeFactor;

    // 4. Clean Void Background (Rings removed, smooth dark gradient + grain only)
    vec2 center = texCoord - 0.5;
    float distFromCenter = length(center);
    
    float grain = proceduralNoise(texCoord * 500.0) * 0.08;
    vec3 voidBg = vec3(0.01, 0.0, 0.02) + grain;

    // 5. Composite Final Output
    vec3 finalRGB = voidBg;
    if (edgeFactor > 0.02) {
        finalRGB = redOutline; 
    }

    // Heavy claustrophobic vignette using math distance
    float vignette = smoothstep(0.8, 0.2, distFromCenter);
    finalRGB *= vignette;

    fragColor = vec4(finalRGB, 1.0);
}
