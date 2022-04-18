package com.abstractphil.masks.masks;

import java.util.HashSet;
import java.util.List;

import com.abstractphil.masks.cfg.MaskData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.DamagerEffect;
import com.redmancometh.reditems.abstraction.SneakEffect;
import com.redmancometh.reditems.abstraction.TickingArmorEffect;

public class PoisonMask implements Mask, DamagerEffect, SneakEffect, TickingArmorEffect {
	private HashSet<LivingEntity> poisonVictim = new HashSet<>();
	private double userDamage = 1;

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
	public void onTick(Player p, int level) {
		try {
			// One second ticks complete, HURT ALL ENTITIES TOUCHED BY POISON!!!
			if (poisonVictim.size() > 0) {
				HashSet<LivingEntity> sliced = new HashSet<>();
				poisonVictim.forEach(entity -> {
					if (entity.getFireTicks() % 20 == 0)
						entity.damage(userDamage * data.getArgs()[1]);
					if (entity.getFireTicks() <= 0)
						sliced.add(entity);
				});
				sliced.forEach(entity -> {
					poisonVictim.remove(entity);
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void hit(EntityDamageEvent e, int level) {
		try {
			if (Math.random() * 100 <= data.getArgs()[0]) // Chance to proc.
			{
				EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
				LivingEntity attacker = (LivingEntity) ev.getDamager();
				userDamage = e.getDamage();
				if (attacker != null) {
					attacker.setFireTicks((int) (20 * data.getArgs()[2]));
					poisonVictim.add(attacker);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		return 1;
	}

	@Override
	public void onSneak(PlayerToggleSneakEvent e, int i) {

	}
}
