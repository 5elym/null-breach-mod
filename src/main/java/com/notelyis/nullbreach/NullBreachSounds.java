package com.notelyis.nullbreach;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class NullBreachSounds {

    public static void playBreachSound(ServerLevel serverWorld, Vec3 spawnPosition) {
        // The heavy, bass-dropping Warden boom (Pitch 1.0)
        serverWorld.playSound(
                null,
                spawnPosition.x, spawnPosition.y, spawnPosition.z,
                SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.MASTER,
                2.0F,
                1.0F);

        // Layered with a deep, warped Enderman teleport sound
        serverWorld.playSound(
                null,
                spawnPosition.x, spawnPosition.y, spawnPosition.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.MASTER,
                1.0F,
                0.5F);

        // Deep, rolling thunder rumble (Low pitch removes the sharp crack)
        serverWorld.playSound(
                null,
                spawnPosition.x, spawnPosition.y, spawnPosition.z,
                SoundEvents.LIGHTNING_BOLT_THUNDER,
                SoundSource.MASTER,
                1.5F,
                0.5F);
    }

    public static void playWhileBreachingSound(ServerLevel serverWorld, ServerPlayer player) {
        int timeLeft = player.tickCount;
        System.out.println("Effect is ticking! Tick: " + player.tickCount);

        // Every 40 ticks (2 seconds) - BLOCK_PORTAL_TRIGGER starts instantly, unlike
        // AMBIENT.
        // Pitched all the way down to 0.1, it sounds like a heavy, suffocating sci-fi
        // wub.
        if (timeLeft % 40 == 0) {
            player.playSound(SoundEvents.PORTAL_TRIGGER, 1.0f, 0.5f);
        }

        // Every 30 ticks (1.5 seconds) - Warden Heartbeat.
        // Deep, terrifying, and pulses directly in their ears.
        if (timeLeft % 30 == 0) {
            player.playSound(SoundEvents.WARDEN_HEARTBEAT, 1.5f, 0.5f);
            player.playSound(SoundEvents.SOUL_ESCAPE.value(), 1.0f, 0.5f);
        }

        // Every 80 ticks (4 seconds) - Illusioner Blindness / Soul whispering.
        // Pitched down, this sounds like a dark, corrupted echoing whisper.
        if (timeLeft % 80 == 0) {
            player.playSound(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 0.5f);
        }
    }

    public static void playStutterSound(ServerLevel serverWorld, Vec3 spawnPosition) {
        // High-pitched electrical zaps (Pitch 2.0)
        // serverWorld.playSound(null, spawnPosition.x, spawnPosition.y,
        // spawnPosition.z,
        // SoundEvents.BEACON_ACTIVATE, SoundSource.MASTER, 0.5f,
        // 2.0f);

        serverWorld.playSound(null, spawnPosition.x, spawnPosition.y, spawnPosition.z,
                SoundEvents.WARDEN_NEARBY_CLOSE, SoundSource.MASTER, 1.5f, 1.0f);
    }

}
