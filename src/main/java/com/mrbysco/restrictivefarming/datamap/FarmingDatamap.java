package com.mrbysco.restrictivefarming.datamap;

import com.mojang.serialization.Codec;
import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

public class FarmingDatamap {

	public static final DataMapType<Block, WhitelistData> CROP_WHITELIST = DataMapType.builder(
					RestrictiveFarmingMod.modLoc("crop_whitelist"),
					Registries.BLOCK, WhitelistData.CODEC)
			.synced(WhitelistData.CODEC, false)
			.build();

	@SubscribeEvent
	public static void register(final RegisterDataMapTypesEvent event) {
		event.register(CROP_WHITELIST);
	}
}
