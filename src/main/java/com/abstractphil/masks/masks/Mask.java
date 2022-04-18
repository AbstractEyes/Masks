package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.*;
import com.abstractphil.masks.util.ItemsUtil;
import com.redmancometh.reditems.RedItems;
import com.redmancometh.reditems.abstraction.TickingArmorEffect;
import com.redmancometh.reditems.abstraction.TransferrableEffect;
import com.redmancometh.reditems.storage.EnchantData;
import com.redmancometh.warcore.util.Pair;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public interface Mask extends TickingArmorEffect, TransferrableEffect {
	public void setData(MaskData data);

	public MaskData getData();
	
	@Override
	public default Pair<Boolean, String> hasBuffType(ItemStack item) {
		List<EnchantData> data = RedItems.getInstance().getEnchantManager().getEffects(item, Mask.class);
		if (data.size() > 0)
			return new Pair(true, "Mask");
		return new Pair(false, null);
	}

	@Override
	default boolean applicableFor(ItemStack item) {
		return ItemsUtil.isHelmet(item);
	}

	@Override
	default List<String> getTransferLore() {
		return getData().getTransferredLore();
	}

	@Override
	default List<String> getLore() {
		return getData().getLore();
	}

	@Override
	default String getName() {
		return getData().getName();
	}
}
