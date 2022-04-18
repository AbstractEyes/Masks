package com.abstractphil.masks;

import com.abstractphil.masks.commands.GiveMaskCommand;
import org.bukkit.plugin.java.JavaPlugin;

//import com.abstractphil.commands.GiveWandCommand;
import com.abstractphil.masks.controller.MaskController;

import lombok.Getter;

@Getter
public class Masks extends JavaPlugin {
	private MaskController maskController;

	@Override
	public void onEnable() {
		super.onEnable();
		this.maskController = new MaskController();
		this.maskController.init();
		getCommand("givemask").setExecutor(new GiveMaskCommand());
	}

	@Override
	public void onDisable() {
		maskController.terminate();
		super.onDisable();
	}

	public static Masks getInstance() {
		return JavaPlugin.getPlugin(Masks.class);
	}
}
