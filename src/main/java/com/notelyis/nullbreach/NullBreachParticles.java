package com.notelyis.nullbreach;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class NullBreachParticles {

        public static void spawnBreachParticles(ServerLevel serverWorld, Vec3 spawnPosition) {

                // Spawn the particle at the calculated position
                serverWorld.sendParticles(net.minecraft.core.particles.ParticleTypes.SONIC_BOOM, spawnPosition.x,
                                spawnPosition.y,
                                spawnPosition.z, 1, 0.0, 0.0,
                                0.0, 0.0);

                // Thick black void smoke exploding outward
                serverWorld.sendParticles(ParticleTypes.SQUID_INK, spawnPosition.x, spawnPosition.y, spawnPosition.z,
                                30, 0.5,
                                0.5, 0.5, 0.1);

                // Purple magical energy lingering in the air
                serverWorld.sendParticles(ParticleTypes.WITCH, spawnPosition.x, spawnPosition.y, spawnPosition.z, 40,
                                0.5, 0.5,
                                0.5, 0.1);

                // The "Pulling" effect - Reverse portal particles suck inward toward the tear
                serverWorld.sendParticles(ParticleTypes.REVERSE_PORTAL, spawnPosition.x, spawnPosition.y,
                                spawnPosition.z, 60,
                                1.5, 1.5, 1.5, 0.05);
        }

        public static void spawnStutterParticles(ServerLevel serverWorld, Vec3 spawnPosition) {
                // // Spawn violent electrical sparks around the player
                // serverWorld.sendParticles(
                // ParticleTypes.ELECTRIC_SPARK,
                // spawnPosition.x, spawnPosition.y, spawnPosition.z,
                // 15,
                // 0.5, 0.5, 0.5, // Spread
                // 0.2);

                // Absolute blackness blinding them
                serverWorld.sendParticles(
                                ParticleTypes.SQUID_INK,
                                spawnPosition.x, spawnPosition.y, spawnPosition.z,
                                5, 0.5, 1.0, 0.5, 0.1);
                // Ghostly souls ripping away from them
                serverWorld.sendParticles(
                                ParticleTypes.CRIMSON_SPORE,
                                spawnPosition.x, spawnPosition.y, spawnPosition.z,
                                3, 0.5, 0.5, 0.5, 0.1);
        }

}
