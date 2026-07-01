package com.mrbysco.restrictivefarming.handler;

import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import com.mrbysco.restrictivefarming.config.FarmingConfig;
import com.mrbysco.restrictivefarming.datamap.FarmingDatamap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class CropHandler {

	@SubscribeEvent
	public static void placeBlock(BlockEvent.EntityPlaceEvent event) {
		if (!FarmingConfig.COMMON.restrictPlacement.get()) return;

		final LevelAccessor level = event.getLevel();
		final BlockState state = event.getPlacedBlock();
		Block block = state.getBlock();
		List<ResourceKey<Biome>> whitelist = getBiomeWhitelist(level.registryAccess(), block);
		RestrictiveFarmingMod.LOGGER.info("{}", whitelist);
		if (whitelist != null) {
			final BlockPos pos = event.getPos();
			final Entity entity = event.getEntity();
			Holder<Biome> biome = level.getBiome(pos);
			if (whitelist.stream().noneMatch(biome::equals)) {
				event.setCanceled(true);
				if (entity instanceof ServerPlayer player) {
					player.sendSystemMessage(Component.translatable("restrictive_farming.restricted_message").withStyle(ChatFormatting.RED), true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void beforeCropGrow(CropGrowEvent.Pre event) {
		if (!FarmingConfig.COMMON.reduceGrowth.get()) return;

		final LevelAccessor level = event.getLevel();
		final BlockPos pos = event.getPos();
		BlockState state = event.getState();
		Block block = state.getBlock();

		float growthReduction = FarmingConfig.COMMON.growthGrowthReduction.get().floatValue();
		List<ResourceKey<Biome>> whitelist = getBiomeWhitelist(level.registryAccess(), block);
		if (whitelist != null) {
			Holder<Biome> biome = level.getBiome(pos);
			if (whitelist.stream().noneMatch(biome::equals) && level.getRandom().nextFloat() < growthReduction) {
				event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
			}
		}
	}

	@SubscribeEvent
	public static void onGrow(BlockGrowFeatureEvent event) {
		if (!FarmingConfig.COMMON.reduceGrowth.get()) return;

		final LevelAccessor level = event.getLevel();
		final BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		float growthReduction = FarmingConfig.COMMON.growthGrowthReduction.get().floatValue();
		List<ResourceKey<Biome>> whitelist = getBiomeWhitelist(level.registryAccess(), block);
		if (whitelist != null) {
			Holder<Biome> biome = level.getBiome(pos);
			if (whitelist.stream().noneMatch(biome::equals) && level.getRandom().nextFloat() < growthReduction) {
				event.setCanceled(true);
			}
		}
	}

	private static List<ResourceKey<Biome>> getBiomeWhitelist(RegistryAccess registryAccess, Block block) {
		List<String> whitelist = block.builtInRegistryHolder().getData(FarmingDatamap.CROP_WHITELIST);
		List<ResourceKey<Biome>> biomeKeys = new ArrayList<>();

		for (String biome : whitelist) {
			// If the biome is a tag, we need to get all biomes in that tag
			if (biome.startsWith("#")) {
				String tag = biome.substring(1);
				var biomeLookup = registryAccess.lookupOrThrow(Registries.BIOME);
				var named = biomeLookup.getOrThrow(TagKey.create(Registries.BIOME, ResourceLocation.tryParse(tag)));
				named.forEach(test -> biomeKeys.add(test.unwrapKey().get()));
			} else {
				biomeKeys.add(ResourceKey.create(Registries.BIOME, ResourceLocation.tryParse(biome)));
			}
		}
		return biomeKeys;
	}
}
