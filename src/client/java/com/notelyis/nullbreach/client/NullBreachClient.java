package com.notelyis.nullbreach.client;

import java.lang.reflect.Method;

import com.notelyis.nullbreach.NullBreach;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class NullBreachClient implements ClientModInitializer {

	// Unique flag so we only load/unload the shader once, not 60 times a second
	private static boolean isShaderActive = false;

	// Pointer to your shader JSON pipeline
	// private static final Identifier NULL_SHADER =
	// Identifier.fromNamespaceAndPath("null-breach",
	// "shaders/post/invert.json");

	@Override
	public void onInitializeClient() {

	}
}
