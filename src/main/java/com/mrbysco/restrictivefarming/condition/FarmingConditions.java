package com.mrbysco.restrictivefarming.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.restrictivefarming.RestrictiveFarmingMod;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FarmingConditions {
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, RestrictiveFarmingMod.MOD_ID);

	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<DefaultRestrictionsCondition>> DEFAULT_RESTRICTIONS = CONDITION_CODECS.register(DefaultRestrictionsCondition.NAME, () -> DefaultRestrictionsCondition.CODEC);

}
