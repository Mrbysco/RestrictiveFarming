package com.mrbysco.restrictivefarming.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import com.mrbysco.restrictivefarming.config.FarmingConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public record WhitelistData(List<String> whitelist, float growthReduction, boolean isCrop) {
	private static final Codec<Float> REDUCTION_CODEC = Codec.FLOAT.validate(value ->
			(value == -1.0f || (value >= 0.0f && value <= 1.0f))
					? DataResult.success(value)
					: DataResult.error(() -> "Value " + value + " must be -1 (default) or in range [0.0, 1.0]")
	);
	public static final Codec<WhitelistData> CODEC = RecordCodecBuilder.create(in -> in.group(
					Codec.STRING.listOf().fieldOf("biomes").forGetter(WhitelistData::whitelist),
					REDUCTION_CODEC.optionalFieldOf("growthReduction", -1.0F).forGetter(WhitelistData::growthReduction),
					Codec.BOOL.optionalFieldOf("isCrop", true).forGetter(WhitelistData::isCrop)
			)
			.apply(in, WhitelistData::new));

	public WhitelistData(List<String> whitelist, float growthReduction) {
		this(whitelist, growthReduction, true);
	}

	public WhitelistData(List<String> whitelist) {
		this(whitelist, -1.0F, true);
	}

	public float getReductionOrDefault() {
		return growthReduction == -1.0F ? FarmingConfig.COMMON.growthReduction.get().floatValue() : growthReduction;
	}

	public List<ResourceKey<Biome>> getBiomeWhitelist(RegistryAccess registryAccess) {
		List<ResourceKey<Biome>> biomeKeys = new ArrayList<>();
		for (String biome : whitelist) {
			// If the biome is a tag, we need to get all biomes in that tag
			if (biome.startsWith("#")) {
				String tag = biome.substring(1);
				var biomeLookup = registryAccess.lookupOrThrow(Registries.BIOME);
				ResourceLocation biomeLoc = ResourceLocation.tryParse(tag);
				if (biomeLoc == null) {
					RestrictiveFarmingMod.LOGGER.error("Invalid biome tag: {}", tag);
					continue;
				}
				var named = biomeLookup.getOrThrow(TagKey.create(Registries.BIOME, biomeLoc));
				named.forEach(test -> test.unwrapKey().ifPresent(biomeKeys::add));
			} else {
				ResourceLocation biomeLoc = ResourceLocation.tryParse(biome);
				if (biomeLoc == null) {
					RestrictiveFarmingMod.LOGGER.error("Invalid biome: {}", biome);
					continue;
				}
				biomeKeys.add(ResourceKey.create(Registries.BIOME, biomeLoc));
			}
		}
		return biomeKeys;
	}
}
