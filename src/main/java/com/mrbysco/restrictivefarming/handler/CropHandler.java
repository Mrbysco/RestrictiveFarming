package com.mrbysco.restrictivefarming.handler;

import com.mrbysco.restrictivefarming.config.FarmingConfig;
import com.mrbysco.restrictivefarming.datamap.FarmingDatamap;
import com.mrbysco.restrictivefarming.datamap.WhitelistData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

@EventBusSubscriber
public class CropHandler {

	@SubscribeEvent
	public static void placeBlock(BlockEvent.EntityPlaceEvent event) {
		if (!FarmingConfig.COMMON.restrictPlacement.get()) return;

		final LevelAccessor level = event.getLevel();
		final BlockState state = event.getPlacedBlock();
		Block block = state.getBlock();
		WhitelistData data = block.builtInRegistryHolder().getData(FarmingDatamap.CROP_WHITELIST);
		if (data != null) {
			final BlockPos pos = event.getPos();
			final Entity entity = event.getEntity();
			Holder<Biome> biome = level.getBiome(pos);
			if (!data.whitelist().contains(biome)) {
				event.setCanceled(true);
				if (entity instanceof ServerPlayer player && FarmingConfig.COMMON.showRestrictedMessage.get()) {
					MutableComponent component = data.isCrop() ?
							Component.translatable("restrictive_farming.restricted_crop_message", block.getName()) :
							Component.translatable("restrictive_farming.restricted_block_message", block.getName());
					player.sendSystemMessage(component.withStyle(ChatFormatting.RED), true);
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

		WhitelistData data = block.builtInRegistryHolder().getData(FarmingDatamap.CROP_WHITELIST);
		if (data != null) {
			float growthReduction = data.getReductionOrDefault();
			Holder<Biome> biome = level.getBiome(pos);
			if (!data.whitelist().contains(biome) && level.getRandom().nextFloat() < growthReduction) {
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

		WhitelistData data = block.builtInRegistryHolder().getData(FarmingDatamap.CROP_WHITELIST);
		if (data != null) {
			float growthReduction = data.getReductionOrDefault();
			Holder<Biome> biome = level.getBiome(pos);
			if (!data.whitelist().contains(biome) && level.getRandom().nextFloat() < growthReduction) {
				event.setCanceled(true);
			}
		}
	}
}
