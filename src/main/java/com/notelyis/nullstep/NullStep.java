package com.notelyis.nullstep;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullStep implements ModInitializer {
	public static final String MOD_ID = "null-step";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<Item> NSD_KEY = ResourceKey.create(Registries.ITEM, id("null_step_device"));
	public static final Item NULL_STEP_DEVICE = new NullStepDeviceItem(
			new Item.Properties().setId(NSD_KEY).stacksTo(1).fireResistant());

	public static final DataComponentType<BlockPos> NSD_POS = DataComponentType.<BlockPos>builder()
			.persistent(BlockPos.CODEC).build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initialising Null Step");

		Registry.register(BuiltInRegistries.ITEM, id("null_step_device"), NULL_STEP_DEVICE);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id("null_step_device_pos"), NSD_POS);

		LOGGER.info("Manufactured Null Step Device");

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
