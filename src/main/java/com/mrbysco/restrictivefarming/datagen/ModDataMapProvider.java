package com.mrbysco.restrictivefarming.datagen;

import com.mrbysco.restrictivefarming.condition.DefaultRestrictionsCondition;
import com.mrbysco.restrictivefarming.datamap.FarmingDatamap;
import com.mrbysco.restrictivefarming.datamap.WhitelistData;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
	public ModDataMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void gather(@NotNull Provider provider) {
		final var biomeLookup = provider.lookupOrThrow(Registries.BIOME);
		final Builder<WhitelistData, Block> whitelist = builder(FarmingDatamap.CROP_WHITELIST);

		List<Block> overworldCrops = List.of(
				Blocks.WHEAT,
				Blocks.CARROTS,
				Blocks.POTATOES,
				Blocks.BEETROOTS,
				Blocks.COCOA,
				Blocks.SWEET_BERRY_BUSH,
				Blocks.MELON_STEM,
				Blocks.PUMPKIN_STEM
		);
		HolderSet.Named<Biome> overworldBiomes = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);
		for (Block crop : overworldCrops) {
			whitelist.add(crop.builtInRegistryHolder(), new WhitelistData(overworldBiomes), false, DefaultRestrictionsCondition.INSTANCE);
		}

		whitelist.add(Blocks.NETHER_WART.builtInRegistryHolder(), new WhitelistData(biomeLookup.getOrThrow(BiomeTags.IS_NETHER)), false, DefaultRestrictionsCondition.INSTANCE);
	}

}
