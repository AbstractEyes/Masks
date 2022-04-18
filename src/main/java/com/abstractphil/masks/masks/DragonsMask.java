package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.MaskData;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.ArmorEffect;
import com.redmancometh.reditems.abstraction.BlockBreakEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.concurrent.ThreadLocalRandom;

// Provides a very low possibility of getting an ender dragon egg while mining
public class DragonsMask implements ArmorEffect, Mask, BlockBreakEffect {
	MaskData data;

	private void rewardDragonEgg(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&6&l[!] &eYour &6Dragon Mask Attachment &e spawned a Dragon Egg in your inventory! SO LUCKY!!!"));
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				"claimgive " + player.getName() + " DragonBreath give {} dragonegg 1");
	}

	@Override
	public void setData(MaskData dataIn) {
		data = dataIn;
	}

	@Override
	public MaskData getData() {
		return data;
	}

	@Override
	public void onTick(Player player, int i) {

	}

	@Override
	public EnchantType getType() {
		return null;
	}

	@Override
	public int getMaxNaturalLevel() {
		return 0;
	}

	@Override
	public void broke(BlockBreakEvent blockBreakEvent, int i) {
		if (blockBreakEvent.getPlayer() == null)
			return;
		if (!blockBreakEvent.getPlayer().getWorld().getName().equals("mineworld"))
			return;
		if (data == null)
			return;
		System.out.println("Dragon egg event event firing.");
		if (ThreadLocalRandom.current().nextDouble(data.getArgs()[0]) <= 1) {
			rewardDragonEgg(blockBreakEvent.getPlayer());
		}
	}

}
