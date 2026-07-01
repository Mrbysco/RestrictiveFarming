package com.mrbysco.restrictivefarming;

import com.mojang.logging.LogUtils;
import com.mrbysco.restrictivefarming.condition.FarmingConditions;
import com.mrbysco.restrictivefarming.config.FarmingConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(RestrictiveFarmingMod.MOD_ID)
public class RestrictiveFarmingMod {
	public static final String MOD_ID = "restrictive_farming";
	public static final Logger LOGGER = LogUtils.getLogger();

	public RestrictiveFarmingMod(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, FarmingConfig.commonSpec);

		FarmingConditions.CONDITION_CODECS.register(eventBus);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static Identifier modLoc(String harvestableCrystals) {
		return Identifier.fromNamespaceAndPath(MOD_ID, harvestableCrystals);
	}
}
