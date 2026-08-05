package com.notelyis.nullbreach;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class NullBreachEffect extends MobEffect {

    private static final int EFFECT_DURATION = 100; // Duration in ticks (5 seconds)
    private static final double PUSH_STRENGTH = 0.5; // Strength of the push when the effect ends
    private static final double PULL_STRENGTH = 2.5; // Strength of the pull when the effect starts
    private static final String FILTER_NAME = "null-breach:null_breach"; // Name of the shader filter to apply

    public NullBreachEffect() {
        super(MobEffectCategory.NEUTRAL, 0x000000);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverWorld, LivingEntity entity, int amplifier) {
        if (entity instanceof ServerPlayer player) {

            // Get the current effect instance to check how much time is left
            var effectInstance = player.getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this));

            if (effectInstance != null) {
                int ticksLeft = effectInstance.getDuration();

                this.applyBreachMessage(player, ticksLeft);
                NullBreachSounds.playWhileBreachingSound(serverWorld, player);

                if (ticksLeft == this.EFFECT_DURATION - 3) {
                    // Allow free movement and noclip
                    player.setGameMode(GameType.SPECTATOR);
                    this.applyNullShader(serverWorld, player, true);
                }

                if (ticksLeft <= this.EFFECT_DURATION - 1 && ticksLeft > 1) {
                    player.setSprinting(false);
                }

                if (ticksLeft <= 40) {
                    // Stuttering effect: Triggers every 5 ticks as it gets closer to zero
                    if (ticksLeft % 5 == 0) {
                        NullBreachParticles.spawnStutterParticles(serverWorld, player.getEyePosition());
                        NullBreachSounds.playStutterSound(serverWorld, player.getEyePosition());
                    }
                }

                if (ticksLeft == 1) {
                    // Should be SURVIVAL, but using CREATIVE for testing
                    player.setGameMode(GameType.SURVIVAL);
                    this.applyPushEffect(player);

                    if (!serverWorld.noCollision(player)) {
                        player.addEffect(
                                new MobEffectInstance(
                                        MobEffects.BLINDNESS,
                                        100,
                                        0,
                                        false,
                                        false,
                                        false));
                        player.hurt(serverWorld.damageSources().source(NullBreach.NULL_DEATH), Float.MAX_VALUE);
                    }

                    this.applyNullShader(serverWorld, player, false);

                    // Vec3 spawnPosition =
                    // player.getEyePosition().add(player.getLookAngle().scale(3.0));
                    NullBreachParticles.spawnBreachParticles(serverWorld, player.getEyePosition());
                    NullBreachSounds.playBreachSound(serverWorld, player.getEyePosition());
                }
            }
        }
        return true;
    }

    private void applyPushEffect(ServerPlayer player) {
        player.setDeltaMovement(player.getLookAngle().x * NullBreachEffect.getPushStrength(),
                NullBreachEffect.getPushStrength(),
                player.getLookAngle().z * NullBreachEffect.getPushStrength());
        player.syncVelocity = true;
    }

    private void applyBreachMessage(ServerPlayer player, int ticksLeft) {
        double percentage = ((double) ticksLeft / this.EFFECT_DURATION) * 100;

        String baseText = "Null Breach Stability: " + (int) percentage + "%";
        MutableComponent finalMessage = Component.empty();

        Random rand = new Random();
        for (int i = 0; i < baseText.length(); i++) {

            String currentLetter = String.valueOf(baseText.charAt(i));
            MutableComponent letterComponent = Component.literal(currentLetter).withStyle(ChatFormatting.RED);

            if (rand.nextInt(90) > percentage) {
                letterComponent.withStyle(ChatFormatting.OBFUSCATED);
            }

            finalMessage.append(letterComponent);
        }

        // Display message above inventory bar
        player.sendSystemMessage(finalMessage, true);
    }

    private void applyNullShader(ServerLevel world, LivingEntity entity, boolean apply) {
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer player) {
            MinecraftServer server = world.getServer();

            if (server != null) {
                server.getCommands().performPrefixedCommand(
                        server.createCommandSourceStack().withSuppressedOutput(),
                        String.format("posteffect %s " + player.getScoreboardName() + " " + this.FILTER_NAME,
                                apply ? "add" : "remove"));
            }
        }
    }

    public static int getEffectDuration() {
        return EFFECT_DURATION;
    }

    public static double getPullStrength() {
        return PULL_STRENGTH;
    }

    public static double getPushStrength() {
        return PUSH_STRENGTH;
    }
}
