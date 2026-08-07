package com.notelyis.nullbreach;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.notelyis.nullbreach.NullBreachEffect;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullBreach implements ModInitializer {
	public static final String MOD_ID = "null-breach";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<Item> NBD_KEY = ResourceKey.create(Registries.ITEM, id("null_breach_device"));
	public static final Item NULL_BREACH_DEVICE = new NullBreachDeviceItem(
			new Item.Properties().setId(NBD_KEY).stacksTo(1).fireResistant());

	public static final DataComponentType<SavedLocation> NBD_SAVED_LOCATION = DataComponentType.<SavedLocation>builder()
			.persistent(SavedLocation.CODEC).build();

	public static final NullBreachEffect NULL_BREACH_EFFECT = new NullBreachEffect();

	public static final ResourceKey<DamageType> NULL_DEATH = ResourceKey.create(Registries.DAMAGE_TYPE,
			id("null_death"));

	public static final SimpleParticleType SPACE_RIP = Registry.register(BuiltInRegistries.PARTICLE_TYPE,
			id("space_rip"), FabricParticleTypes.simple());

	public static final ResourceKey<CreativeModeTab> NULL_BREACH_TAB_KEY = ResourceKey
			.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id("null_breach_tab"));

	public static final CreativeModeTab NULL_BREACH_TAB = FabricCreativeModeTab.builder()
			.icon(() -> new ItemStack(NULL_BREACH_DEVICE))
			.title(Component.translatable("creativeTab.null-breach.null_breach_tab"))
			.displayItems((params, output) -> {
				output.accept(NULL_BREACH_DEVICE); // Add future items here!
			})
			.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initialising Null Breach");

		Registry.register(BuiltInRegistries.ITEM, id("null_breach_device"), NULL_BREACH_DEVICE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id("nbd_saved_location"),
				NBD_SAVED_LOCATION);
		Registry.register(BuiltInRegistries.MOB_EFFECT, id("null_breach_effect"), NULL_BREACH_EFFECT);
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, NULL_BREACH_TAB_KEY, NULL_BREACH_TAB);

		LOGGER.info("Manufactured Null Breach Device");

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
