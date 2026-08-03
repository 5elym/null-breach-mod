#version 450


layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

uniform sampler2D DiffuseSampler;

void main() {
    // Simply takes the picture and draws it exactly as is
    fragColor = texture(DiffuseSampler, texCoord);
}
