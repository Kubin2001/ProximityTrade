package org.ptrade.proximityTrade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LogPack {
    private boolean valid = false;
    private String playerName = null;
    private String partnerName = null;
    private ArrayList<ItemStack> playerItems = null;
    private ArrayList<ItemStack> partnerItems = null;
    private int moneyPlayer = 0;
    private int moneyPartner = 0;
    private int levelPlayer = 0;
    private int levelPartner = 0;

    public LogPack(Player player, Player partner, TradeStatus playerStatus, TradeStatus partnerStatus,
                   ArrayList<ItemStack> playerItems, ArrayList<ItemStack> partnerItems) {
        if (playerStatus == null || partnerStatus == null || playerItems == null || partnerItems == null) {
            valid = false;
            return;
        }
        playerName = player.getName();
        partnerName = partner.getName();
        moneyPlayer = (int)playerStatus.money;
        moneyPartner = (int)partnerStatus.money;
        levelPlayer = playerStatus.xpVal;
        levelPartner = partnerStatus.xpVal;
        this.playerItems = playerItems;
        this.partnerItems = partnerItems;

        valid = true;
    }

    public Log(){

    }
}
