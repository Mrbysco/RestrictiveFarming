package com.mrbysco.restrictivefarming.datagen;

import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RestrictiveFarmingMod.MOD_ID)
public class FarmingDataGen {

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();

		gen.addProvider(true, new ModDataMapProvider(output, lookupProvider));

		gen.addProvider(true, new FarmingLanguageProvider(output));
	}
}
