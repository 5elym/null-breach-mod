package com.notelyis.nullstep;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public record SavedLocation(Vec3 pos, float yaw, float pitch) {
    // Saves the player's position, yaw, and pitch when the NSD was activated.
    public static final Codec<SavedLocation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("pos").forGetter(SavedLocation::pos),
            Codec.FLOAT.fieldOf("yaw").forGetter(SavedLocation::yaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(SavedLocation::pitch)).apply(instance, SavedLocation::new));
}
