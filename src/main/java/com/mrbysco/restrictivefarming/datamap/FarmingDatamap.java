package com.mrbysco.restrictivefarming.datamap;

import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber
public class FarmingDatamap {
	public static final DataMapType<Block, HolderSet<Biome>> CROP_WHITELIST = DataMapType.builder(
					RestrictiveFarmingMod.modLoc("crop_whitelist"),
					Registries.BLOCK, RegistryCodecs.homogeneousList(Registries.BIOME))
			.synced(RegistryCodecs.homogeneousList(Registries.BIOME), false)
			.build();

	@SubscribeEvent
	public static void register(final RegisterDataMapTypesEvent event) {
		event.register(CROP_WHITELIST);
	}
}
