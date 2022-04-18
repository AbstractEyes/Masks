package com.abstractphil.masks.masks;

import com.abstractphil.masks.cfg.MaskData;
import com.abstractphil.masks.util.ItemsUtil;
import com.abstractphil.pumpkin.PumpkinMain;
import com.abstractphil.pumpkin.effects.PumpkinAxeEffect;
import com.abstractphil.pumpkin.util.AbsPhilItemUtils;
import com.redmancometh.reditems.EnchantType;
import com.redmancometh.reditems.abstraction.ArmorEffect;
import com.redmancometh.reditems.abstraction.AttachmentEffect;
import com.redmancometh.reditems.abstraction.BlockBreakEffect;
import com.redmancometh.reditems.abstraction.ClickEffect;
import com.redmancometh.warcore.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class PumpkinMask implements Mask, ArmorEffect, BlockBreakEffect {
    private MaskData data;

    private AbsPhilItemUtils getUtils() {
        return PumpkinMain.getInstance().getMainController().getUtils();
    }

    @Override
    public void broke(BlockBreakEvent blockBreakEvent, int i) {
        if(Bukkit.getPluginManager().isPluginEnabled("PumpkinAxe")) {
            Player player = blockBreakEvent.getPlayer();
            if(blockBreakEvent.getPlayer() == null) {
                if(getUtils().isPumpkinAxe(player.getItemInHand())) {
                    int fortuneValue = 3;
                    for (ItemStack drop : blockBreakEvent.getBlock().getDrops()) {
                        int outCount = ThreadLocalRandom.current().nextInt(1, (int) Math.ceil(fortuneValue * 2));
                        blockBreakEvent.getPlayer().getInventory().addItem(new ItemStack(drop.getType(), outCount));
                        ItemStack item = blockBreakEvent.getPlayer().getItemInHand();
                        getUtils().addSafeJsonAmountInt(blockBreakEvent.getPlayer(), item, "pfort", fortuneValue, true);
                    }
                }
            }
        }
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
    public EnchantType getType() {
        return null;
    }

    @Override
    public int getMaxNaturalLevel() {
        return 0;
    }

    @Override
    public boolean applicableFor(ItemStack item) {
        return ItemsUtil.isHelmet(item);
    }

    @Override
    public void onTick(Player player, int i) {

    }
}
