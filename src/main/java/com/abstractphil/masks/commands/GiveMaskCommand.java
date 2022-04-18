package com.abstractphil.masks.commands;

import com.abstractphil.masks.Masks;
import com.abstractphil.masks.cfg.MaskData;
import com.abstractphil.masks.controller.MaskController;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveMaskCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp())
			return false;
		try {
			MaskController masks = Masks.getInstance().getMaskController();
			Player p = Bukkit.getPlayer(args[0]);
			MaskData data = masks.getMaskData(args[1]);
			p.getInventory().addItem(data.getItem().clone());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
