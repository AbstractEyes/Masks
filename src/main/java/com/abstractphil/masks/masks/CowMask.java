package com.abstractphil.masks.masks;

import java.util.Collection;
import java.util.HashSet;

import com.abstractphil.masks.cfg.MaskData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.SneakEffect;
import com.redmancometh.reditems.abstraction.TickingArmorEffect;

public class CowMask implements Mask, SneakEffect, TickingArmorEffect {
	@SuppressWarnings("serial")
	HashSet<PotionEffectType> negativeEffects = new HashSet<PotionEffectType>() {
		{
			add(PotionEffectType.BLINDNESS);
			add(PotionEffectType.CONFUSION);
			add(PotionEffectType.POISON);
			add(PotionEffectType.HUNGER);
			add(PotionEffectType.SLOW);
			add(PotionEffectType.WEAKNESS);
		}
	};

	private MaskData data;

	@Override
	public void setData(MaskData data) {
		this.data = data;
	}

	@Override
	public MaskData getData() {
		return data;
	}

	private int tickTime = 20;

	@Override
	public void onTick(Player p, int level) {
		try
		{
			tickTime -= 1;
			if (tickTime <= 0) {
				tickTime = (int) data.getArgs()[1];
				Player pl = p.getPlayer();
				if (Math.random() * 99 < data.getArgs()[0] * 100) {
					// roll successful, find a matching potion effect in the array.
					if (pl != null) {
						if(pl.getActivePotionEffects().size() > 0)
						{
							HashSet<PotionEffectType> matching = effectTypes(pl.getActivePotionEffects());
							matching.retainAll(negativeEffects);
							int rand = (int) (Math.random() * matching.size());
							pl.removePotionEffect((PotionEffectType) matching.toArray()[rand]);
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private HashSet<PotionEffectType> effectTypes(Collection<PotionEffect> activeEffects) {
		HashSet<PotionEffectType> outSet = new HashSet<>();
		activeEffects.forEach(potionEffect -> {
			outSet.add(potionEffect.getType());
		});
		return outSet;
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
		return 1;
	}

	@Override
	public void onSneak(PlayerToggleSneakEvent playerToggleSneakEvent, int i) {

	}
}
