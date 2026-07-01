package com.mrbysco.restrictivefarming.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FarmingConfig {

	public static class Common {
		public final ModConfigSpec.BooleanValue defaultRestrictions;
		public final ModConfigSpec.BooleanValue restrictPlacement;
		public final ModConfigSpec.BooleanValue reduceGrowth;
		public final ModConfigSpec.DoubleValue growthGrowthReduction;

		Common(ModConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			defaultRestrictions = builder
					.comment("Whether to enable the default dimension restrictions for crops.")
					.define("defaultRestrictions", true);

			restrictPlacement = builder
					.comment("Whether to restrict crop placement to whitelisted biomes.")
					.define("restrictPlacement", true);

			reduceGrowth = builder
					.comment("Whether to reduce crop growth in non-whitelisted biomes.")
					.define("reduceGrowth", true);

			growthGrowthReduction = builder
					.comment("The percentage of growth reduction for crops in non-whitelisted biomes.")
					.defineInRange("growthGrowthReduction", 0.5, 0.0, 1.0);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
