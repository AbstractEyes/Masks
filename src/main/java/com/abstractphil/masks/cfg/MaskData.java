package com.abstractphil.masks.cfg;

import com.abstractphil.masks.Masks;
import com.abstractphil.masks.controller.MaskController;
import com.abstractphil.masks.masks.Mask;
import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import com.redmancometh.reditems.RedItems;
import com.redmancometh.reditems.mediator.AttachmentManager;
import com.redmancometh.warcore.util.ItemUtil;

import lombok.Data;
import net.minecraft.server.v1_8_R3.TileEntitySkull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class MaskData {
	private Class<? extends Mask> maskClass;
	private String name, displayName;
	private List<String> lore, transferredLore;
	private Material maskMaterial;
	private double[] args;
	private String skullOwner;
	// Protected to prevent gson serialization
	protected ItemStack item;
	protected GameProfile profile;
	protected static Field profileField;

	static {
		try {
			// Have to do it this way since CraftMetaSkull is inaccessible directly.
			ItemStack baseItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) baseItem.getItemMeta();
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		GameProfile profile = new GameProfile(null, skullOwner);
		TileEntitySkull.b(profile, new Predicate<GameProfile>() {
			@Override
			public boolean apply(GameProfile completedProfile) {
				MaskData.this.profile = completedProfile;
				return true;
			}
		});
	}

	public Mask getMaskInstance() {
		MaskController masks = Masks.getInstance().getMaskController();
		for (Mask mask : masks.getMaskMap().values()) {
			if (mask.getClass().equals(maskClass)) {
				return mask;
			}
		}
		return null;
	}

	public ItemStack getItem() {
		if (item != null)
			return item;
		try {
			AttachmentManager am = RedItems.getInstance().getAttachManager();
			ItemStack baseItem = new ItemStack(maskMaterial, 1, (short) 3);
			SkullMeta meta = (SkullMeta) baseItem.getItemMeta();
			profileField.set(meta, profile);
			baseItem.setItemMeta(meta);
			ItemUtil.setName(baseItem, getDisplayName());
			ItemStack mask = RedItems.getInstance().getEnchantManager().attachEffect(baseItem, getMaskInstance(), 1);
			mask = am.makeAttachment(mask);
			this.item = mask;
			return this.item;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
