package com.mrbysco.restrictivefarming.datagen;

import com.mrbysco.restrictivefarming.condition.DefaultRestrictionsCondition;
import com.mrbysco.restrictivefarming.datamap.FarmingDatamap;
import com.mrbysco.restrictivefarming.datamap.WhitelistData;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
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
		for (Block crop : overworldCrops) {
			addCrop(crop, BiomeTags.IS_OVERWORLD, whitelist);
		}

		addCrop(Blocks.NETHER_WART, BiomeTags.IS_NETHER, whitelist);
	}

	private void addCrop(Block block, TagKey<Biome> biomeTag, Builder<WhitelistData, Block> whitelist) {
		whitelist.add(block.builtInRegistryHolder(), new WhitelistData(List.of("#" + biomeTag.location())), false, DefaultRestrictionsCondition.INSTANCE);
	}

}
