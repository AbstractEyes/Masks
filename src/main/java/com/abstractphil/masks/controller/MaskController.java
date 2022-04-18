package com.abstractphil.masks.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.abstractphil.masks.cfg.MaskData;
import com.abstractphil.masks.masks.Mask;
import com.abstractphil.masks.util.ItemsUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.redmancometh.configcore.config.ConfigManager;
import com.redmancometh.reditems.RedItems;
import com.redmancometh.reditems.mediator.EnchantManager;
import com.redmancometh.reditems.storage.EnchantData;
import com.redmancometh.reditems.util.RedItemUtil;
import com.redmancometh.warcore.util.ItemUtil;

import lombok.Getter;

import com.abstractphil.masks.Masks;
import com.abstractphil.masks.cfg.MaskConfig;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;

public class MaskController {
	private ConfigManager<MaskConfig> cfg = new ConfigManager("masks.json", MaskConfig.class);
	@Getter
	private Map<String, Mask> maskMap = new ConcurrentHashMap();

	public void init() {
		cfg.init();
		Map<String, MaskData> masks = cfg.getConfig().getMasks();
		masks.forEach((name, mask) -> {
			try {
				mask.init();
				Mask maskInst = mask.getMaskClass().newInstance();
				maskInst.setData(mask);
				maskMap.put(name, maskInst);
				RedItems.getInstance().getEnchantManager().registerEffect(maskInst);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		registerProtocolListener();
	}

	public void registerProtocolListener() {
		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(Masks.getInstance(), ListenerPriority.NORMAL,
						PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Server.SET_SLOT,
						PacketType.Play.Server.WINDOW_ITEMS) {
					@Override
					public void onPacketSending(PacketEvent event) {
						PacketContainer packet = event.getPacket();
						PacketType packetType = event.getPacketType();
						if (packetType == PacketType.Play.Server.ENTITY_EQUIPMENT) {
							handlePlayEquip(packet);
							return;
						}
						if (packetType == PacketType.Play.Server.WINDOW_ITEMS) {
							handleWindowItems(packet, event);
							return;
						}
						if (packetType == PacketType.Play.Server.SET_SLOT) {
							handleSetSlot(packet, event);
						}
					}
				});
	}

	public void handleWindowItems(PacketContainer packet, PacketEvent event) {
		EnchantManager em = RedItems.getInstance().getEnchantManager();
		int windowId = packet.getIntegers().read(0);
		if (windowId != ((CraftPlayer) event.getPlayer()).getHandle().defaultContainer.windowId)
			return;
		ItemStack[] itemStacks = packet.getItemArrayModifier().read(0);
		if (itemStacks == null)
			return;
		ItemStack item = itemStacks[5];
		if (!em.isRedItem(item) || !ItemsUtil.isHelmet(item))
			return;
		List<EnchantData> data = em.getEffects(item, Mask.class);
		if (data.size() > 0) {
			Mask mask = (Mask) data.get(0).getEffect();
			ItemStack maskItem = mask.getData().getItem().clone();
			if (item.getItemMeta().hasDisplayName())
				ItemUtil.setName(maskItem, item.getItemMeta().getDisplayName());
			if (item.getItemMeta().hasLore())
				maskItem = ItemUtil.setLore(maskItem, item.getItemMeta().getLore());
			itemStacks[5] = maskItem;
			packet.getItemArrayModifier().write(0, itemStacks);
		}
	}

	public void handleSetSlot(PacketContainer packet, PacketEvent event) {
		EnchantManager em = RedItems.getInstance().getEnchantManager();
		int slot = packet.getIntegers().read(1);
		if (slot != 5)
			return;
		int windowId = packet.getIntegers().read(0);
		if (windowId != ((CraftPlayer) event.getPlayer()).getHandle().defaultContainer.windowId)
			return;
		ItemStack item = packet.getItemModifier().read(0);
		if (!em.isRedItem(item) || !ItemsUtil.isHelmet(item))
			return;
		List<EnchantData> data = em.getEffects(item, Mask.class);
		if (data.size() > 0) {
			EnchantData buffData = data.get(0);
			Mask mask = (Mask) data.get(0).getEffect();
			ItemStack maskItem = mask.getData().getItem().clone();
			if (item.getItemMeta().hasDisplayName())
				ItemUtil.setName(maskItem, item.getItemMeta().getDisplayName());
			if (item.getItemMeta().hasLore())
				ItemUtil.setLore(maskItem, item.getItemMeta().getLore());
			if (item.getItemMeta().hasEnchants())
				item.getItemMeta().getEnchants().forEach((ench, level) -> maskItem.addUnsafeEnchantment(ench, level));
			RedItemUtil.removeLore(maskItem, buffData.getEffectLore());
			packet.getItemModifier().write(0, maskItem);
		}
	}

	public void handlePlayEquip(PacketContainer packet) {
		EnchantManager em = RedItems.getInstance().getEnchantManager();
		int slot = packet.getIntegers().read(1);
		if (slot != 4)
			return;
		ItemStack item = packet.getItemModifier().read(0);
		if (!em.isRedItem(item) || !ItemsUtil.isHelmet(item))
			return;
		List<EnchantData> data = em.getEffects(item, Mask.class);
		if (data.size() > 0) {
			Mask mask = (Mask) data.get(0).getEffect();
			ItemStack maskItem = mask.getData().getItem();
			packet.getItemModifier().write(0, maskItem);
		}
		return;
	}

	public void handleEntityEquipment(PacketEvent event, PacketContainer packet) {

	}

	public MaskData getMaskData(String maskName) {
		return cfg.getConfig().getMasks().get(maskName);
	}

	public Material getMaskMaterial(String maskName) {
		return getMask(maskName).getData().getMaskMaterial();
	}

	public Mask getMask(String maskName) {
		return maskMap.get(maskName);
	}

	public MaskConfig cfg() {
		return cfg.getConfig();
	}

	public void terminate() {
		maskMap.values().forEach((mask) -> RedItems.getInstance().getEnchantManager().deregisterEffect(mask));
	}
}
