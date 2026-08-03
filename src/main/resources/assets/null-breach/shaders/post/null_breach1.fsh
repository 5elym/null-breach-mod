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

// --- Helper: Cheap Noise Function ---
float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec2 tc = texCoord;
    
    // Convert normalized GameTime (0.0-1.0) into incredibly fast raw ticks
    float fastTime = GameTime * 24000.0; 
    
    float shakeSpeed = 1.0; // You can lower this now that fastTime is so massive
    float shakeAmount = 0.003;
    
    // Apply erratic jitter using fastTime
    tc.x += (rand(vec2(fastTime * shakeSpeed, 1.0)) * 2.0 - 1.0) * shakeAmount;
    tc.y += (rand(vec2(1.0, fastTime * shakeSpeed)) * 2.0 - 1.0) * shakeAmount;

    // 2. Sample World Information
    vec4 centerCol = texture(DiffuseSampler, tc);

    // 3. Simple Edge Detection (Outline logic)
    float offset = 0.002;
    float up    = dot(texture(DiffuseSampler, tc + vec2(0.0, offset)).rgb,    vec3(0.299, 0.587, 0.114));
    float down  = dot(texture(DiffuseSampler, tc - vec2(0.0, offset)).rgb,    vec3(0.299, 0.587, 0.114));
    float left  = dot(texture(DiffuseSampler, tc - vec2(offset, 0.0)).rgb,    vec3(0.299, 0.587, 0.114));
    float right = dot(texture(DiffuseSampler, tc + vec2(offset, 0.0)).rgb,    vec3(0.299, 0.587, 0.114));
    
    float edgeFactor = abs(up - down) + abs(right - left);
    edgeFactor = smoothstep(0.05, 0.3, edgeFactor); 
    
    vec3 redOutline = vec3(0.8, 0.0, 0.0) * edgeFactor;

    // 4. Create the Contorting and Pulsing Void Background
    // Use fastTime here as well for the sin waves!
    float pulse = (sin(fastTime * 0.15) * 0.5 + 0.5) * 0.1;
    float voidContortion = rand(tc * (2.0 + sin(fastTime * 0.05))) * 0.05;
    
    vec3 voidBg = mix(vec3(0.005), vec3(0.05, 0.0, 0.08), 0.3) + pulse + voidContortion;

    // 5. Composite Final Output
    vec3 finalRGB = voidBg;
    if (edgeFactor > 0.01) {
        finalRGB = redOutline; 
    }

    float distFromCenter = distance(texCoord, vec2(0.5));
    float vignette = 1.0 - smoothstep(0.3, 0.7, distFromCenter);
    
    finalRGB *= vignette;

    fragColor = vec4(finalRGB, 1.0);
}
