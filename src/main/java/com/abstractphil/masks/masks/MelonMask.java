package com.abstractphil.masks.masks;

//todo: lifetap mask
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.abstractphil.masks.cfg.MaskData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.ArmorEffect;
import com.redmancometh.reditems.abstraction.ClickEffect;

public class MelonMask implements ArmorEffect, ClickEffect, Mask {
	MaskData data;

	private LoadingCache<UUID, Long> rightCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS)
			.build(new CacheLoader<UUID, Long>() {
				@Override
				public Long load(UUID arg0) throws Exception {
					return 0L;
				}
			});

	@Override
	public int getMaxNaturalLevel() {
		return 0;
	}

	@Override
	public EnchantType getType() {
		return null;
	}

	@Override
	public void onTick(Player arg0, int arg1) {

	}

	@Override
	public void onLeftClick(PlayerInteractEvent arg0, int arg1) {

	}

	@Override
	public void onRightClick(PlayerInteractEvent event, int arg1) {
		Player p = event.getPlayer();
		long remaining = getRemainingCooldown(p.getUniqueId());
		if (remaining <= 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100 * arg1, arg1 + 1));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&e&l[&4!&e] &r&aLifewell has been used! It will be on cooldown for &r&4&l60&r&a seconds!"));
			rightCache.put(p.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60));
			return;
		}
		sendCooldownMessage(p, remaining);

	}

	public void sendCooldownMessage(Player p, long remaining) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&l[&4!&e] &r&eLifewell is on cooldown for another &r&4&l" + remaining + "&r&e seconds!"));
	}

	public long getRemainingCooldown(UUID uuid) {
		try {
			return (rightCache.get(uuid) - System.currentTimeMillis()) / 1000;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void setData(MaskData dataIn) {
		data = dataIn;
	}

	@Override
	public MaskData getData() {
		return data;
	}
}