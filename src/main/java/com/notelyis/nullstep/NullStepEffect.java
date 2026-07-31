package com.notelyis.nullstep;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class NullStepEffect extends MobEffect {

    private static final int EFFECT_DURATION = 100; // Duration in ticks (5 seconds)
    private static final double PUSH_STRENGTH = 0.5; // Strength of the push when the effect ends
    private static final double PULL_STRENGTH = 2.5; // Strength of the pull when the effect starts

    public NullStepEffect() {
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

                if (ticksLeft == this.EFFECT_DURATION - 3) {
                    // Allow free movement and noclip
                    player.setGameMode(GameType.SPECTATOR);
                }

                if (ticksLeft <= this.EFFECT_DURATION - 1 && ticksLeft > 1) {
                    player.setSprinting(false);
                }

                if (ticksLeft == 1) {
                    // Should be SURVIVAL, but using CREATIVE for testing
                    player.setGameMode(GameType.SURVIVAL);
                    player.setDeltaMovement(player.getLookAngle().x * NullStepEffect.getPushStrength(),
                            NullStepEffect.getPushStrength(),
                            player.getLookAngle().z * NullStepEffect.getPushStrength());
                    player.hurtMarked = true;
                    player.sendSystemMessage(Component.literal("Reality Restored!"));

                    if (!serverWorld.noCollision(player)) {
                        player.addEffect(
                                new MobEffectInstance(
                                        MobEffects.BLINDNESS,
                                        100,
                                        0,
                                        false,
                                        false,
                                        false));
                        player.hurt(serverWorld.damageSources().source(NullStep.NULL_DEATH), Float.MAX_VALUE);
                    }
                }
            }
        }
        return true;
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
