package com.mrbysco.restrictivefarming.datagen;

import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jspecify.annotations.Nullable;

public class FarmingLanguageProvider extends LanguageProvider {
	public FarmingLanguageProvider(PackOutput packOutput) {
		super(packOutput, "restrictivefarming", "en_us");
	}

	@Override
	protected void addTranslations() {
		add("restrictive_farming.restricted_crop_message", "You cannot plant this crop in this biome!");
		add("restrictive_farming.restricted_block_message", "You cannot place this block in this biome!");

		addConfig("general", "General", "General Settings");
		addConfig("defaultRestrictions", "Default Restrictions", "Whether to enable the default dimension restrictions for crops.");
		addConfig("restrictPlacement", "Restrict Placement", "Whether to restrict crop placement to whitelisted biomes.");
		addConfig("reduceGrowth", "Reduce Growth", "Whether to reduce crop growth in non-whitelisted biomes.");
		addConfig("growthGrowthReduction", "Growth Reduction", "The percentage of growth reduction for crops in non-whitelisted biomes.");

	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add(RestrictiveFarmingMod.MOD_ID + ".configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add(RestrictiveFarmingMod.MOD_ID + ".configuration." + path + ".tooltip", description);
	}
}
