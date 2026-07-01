package com.mrbysco.restrictivefarming.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.restrictivefarming.config.FarmingConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class DefaultRestrictionsCondition implements ICondition {

	public static final String NAME = "default_restrictions";

	public static final DefaultRestrictionsCondition INSTANCE = new DefaultRestrictionsCondition();

	public static MapCodec<DefaultRestrictionsCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	@Override
	public boolean test(IContext context) {
		return FarmingConfig.COMMON.defaultRestrictions.get();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return FarmingConditions.DEFAULT_RESTRICTIONS.get();
	}

	@Override
	public String toString() {
		return NAME;
	}
}