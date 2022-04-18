package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.MaskData;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.SneakEffect;
import com.redmancometh.reditems.abstraction.TickingArmorEffect;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class IronGolemMask implements Mask, TickingArmorEffect, SneakEffect {
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
						new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 25, (int) data.getArgs()[0]), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, (int) data.getArgs()[1]),
						true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onSneak(PlayerToggleSneakEvent playerToggleSneakEvent, int i) {

	}

	@Override
	public List<String> getLore() {
		return data.getLore();
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public EnchantType getType() {
		return EnchantType.HELMET;
	}

	@Override
	public int getMaxNaturalLevel() {
		return 0;
	}
}
