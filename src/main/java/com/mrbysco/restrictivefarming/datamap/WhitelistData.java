package com.mrbysco.restrictivefarming.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.restrictivefarming.config.FarmingConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

public record WhitelistData(HolderSet<Biome> whitelist, float growthReduction, boolean isCrop) {
	private static final Codec<Float> REDUCTION_CODEC = Codec.FLOAT.validate(value ->
			(value == -1.0f || (value >= 0.0f && value <= 1.0f))
					? DataResult.success(value)
					: DataResult.error(() -> "Value " + value + " must be -1 (default) or in range [0.0, 1.0]")
	);
	public static final Codec<WhitelistData> CODEC = RecordCodecBuilder.create(in -> in.group(
					RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(WhitelistData::whitelist),
					REDUCTION_CODEC.optionalFieldOf("growthReduction", -1.0F).forGetter(WhitelistData::growthReduction),
					Codec.BOOL.optionalFieldOf("isCrop", true).forGetter(WhitelistData::isCrop)
			)
			.apply(in, WhitelistData::new));

	public WhitelistData(HolderSet<Biome> whitelist, float growthReduction) {
		this(whitelist, growthReduction, true);
	}

	public WhitelistData(HolderSet<Biome> whitelist) {
		this(whitelist, -1.0F, true);
	}

	public float getReductionOrDefault() {
		return growthReduction == -1.0F ? FarmingConfig.COMMON.growthReduction.get().floatValue() : growthReduction;
	}
}
