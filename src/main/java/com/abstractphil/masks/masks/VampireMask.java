package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.MaskData;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.DamagerEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.List;

public class VampireMask implements Mask, DamagerEffect {
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
	public void hit(EntityDamageEvent e, int level) {
		try {
			EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
			System.out.println("Hit living entity while wearing mask.");
			LivingEntity attacker = (LivingEntity) ev.getDamager();
			if (attacker instanceof Player) {
				if (Math.random() * 99 < data.getArgs()[0] * 100) {
					attacker.sendMessage("Vampiric proc, " + data.getArgs()[1] + " heart healed.");
					attacker.setHealth(attacker.getHealth() + data.getArgs()[1]);
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
	public void onTick(Player arg0, int arg1) {

	}
}
