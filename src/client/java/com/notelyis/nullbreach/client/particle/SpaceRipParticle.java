package com.notelyis.nullbreach.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SingleQuadParticle;

public class SpaceRipParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    protected SpaceRipParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.get(0, 0)); // Start with the first sprite
        this.sprites = sprites;

        // The tear lasts for 30 ticks (1.5 seconds)
        this.lifetime = 30;

        // Starts completely invisible/tiny
        this.quadSize = 0.0f;

        // Don't let gravity pull the tear down, and don't collide with walls
        this.gravity = 0.0f;
        this.hasPhysics = false;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        // Calculate how far along the animation is (0.0 to 1.0)
        float progress = (float) this.age / this.lifetime;

        // Aggressively tear open: Fast scale up, then slow down
        // Uses a basic ease-out math trick: 1.0 - (1.0 - x)^3
        float easeOut = 1.0f - (float) Math.pow(1.0 - progress, 3);

        // The tear will grow to be 4 blocks wide
        this.quadSize = easeOut * 4.0f;

        // Optional: Fade out smoothly at the very end
        if (progress > 0.8f) {
            this.alpha = 1.0f - ((progress - 0.8f) * 5.0f); // Fades from 1.0 to 0.0 in the last 20%
        }
    }

    @Override
    protected Layer getLayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLayer'");
    }
}
