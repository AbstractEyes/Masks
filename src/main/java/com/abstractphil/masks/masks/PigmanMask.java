package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.MaskData;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.SneakEffect;
import com.redmancometh.reditems.abstraction.TickingArmorEffect;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PigmanMask implements Mask, TickingArmorEffect, SneakEffect {
	private MaskData data;

	@Override
	public void setData(MaskData data) {
		this.data = data;
	}

	@Override
	public MaskData getData() {
		return data;
	}

	@Override
	public void onTick(Player player, int i) {
		try {
			if (player != null) {
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, (int) data.getArgs()[0], true), true);
				player.addPotionEffect(
						new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 25, (int) data.getArgs()[1], true), true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onSneak(PlayerToggleSneakEvent playerToggleSneakEvent, int i) {

	}

	@Override
	public EnchantType getType() {
		return null;
	}

	@Override
	public int getMaxNaturalLevel() {
		return 0;
	}
}
