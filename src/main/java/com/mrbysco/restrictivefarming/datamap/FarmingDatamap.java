package com.mrbysco.restrictivefarming.datamap;

import com.mojang.serialization.Codec;
import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

public class FarmingDatamap {
	public static final DataMapType<Block, List<String>> CROP_WHITELIST = DataMapType.builder(
					RestrictiveFarmingMod.modLoc("crop_whitelist"),
					Registries.BLOCK, Codec.STRING.listOf())
			.synced(Codec.STRING.listOf(), false)
			.build();

	@SubscribeEvent
	public static void register(final RegisterDataMapTypesEvent event) {
		event.register(CROP_WHITELIST);
	}
}
