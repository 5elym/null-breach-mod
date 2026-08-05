package com.notelyis.nullbreach.client.particle;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import com.notelyis.nullbreach.client.particle.SpaceRipParticle;

public class SpaceRipParticleFactory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public SpaceRipParticleFactory(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
            double xAux,
            double yAux, double zAux, RandomSource random) {
        return new SpaceRipParticle(level, x, y, z, this.sprites);
    }
}
