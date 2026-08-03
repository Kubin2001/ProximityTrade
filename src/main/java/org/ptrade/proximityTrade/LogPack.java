package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.time.LocalDateTime;
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

    private String ItemsToString(ArrayList<ItemStack> items){
        StringBuilder itemMsg = new StringBuilder();
        for(ItemStack item : items){
            itemMsg.append(item.getType()).append(" ").append(item.getAmount()).append("\n");
        }
        return itemMsg.toString();
    }

    private void SendLog(String log){
        Plugin plugin = Helpers.plugin;
        if (MainConfig.logOutput == LogOutput.File) {
            File folder = new File(plugin.getDataFolder(), "Logs");

            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    plugin.getLogger().warning("Cannot create Logs folder!");
                    return;
                }
            }

            LocalDateTime now = LocalDateTime.now();
            File logFile = new File(folder, now.getDayOfYear() + "_" + now.getYear() + ".log");
            try {
                if (!logFile.exists()) {
                    if (!logFile.createNewFile()) {
                        plugin.getLogger().warning("Cannot create log file!");
                        return;
                    }
                }

                try (FileWriter fw = new FileWriter(logFile, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {

                    out.println(log);
                    out.println();
                }

            } catch (IOException e) {
                plugin.getLogger().severe("Cannot write to log file: " + logFile.getName());
                e.printStackTrace();
            }
        }
        else{
           Helpers.plugin.getLogger().info(log);
        }
    }

    public LogPack(Player player, Player partner,
                   TradeStatus playerStatus, TradeStatus partnerStatus,
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

    public void Log(boolean fail){
        Bukkit.getScheduler().runTaskAsynchronously(Helpers.plugin, () ->{

            StringBuilder msg = new StringBuilder();

            LocalDateTime now = LocalDateTime.now();
            msg.append("\n---------------------------------\n");
            msg.append("Date:").append(now.getHour()).append(":").append(now.getMinute()).append("  ").
                    append(now.getDayOfMonth()).append(":").append(now.getMonth()).append(":").append(now.getYear()).append("\n");

            if(!valid){
                msg.append("Invalid\n");
                SendLog(msg.toString());
                return;
            }
            if(fail){
                msg.append("Trade failed due to inability to pay tax\n");
                SendLog(msg.toString());
                return;
            }

            msg.append(playerName).append("\n");
            msg.append("Money: ").append(moneyPlayer).append("\n");
            msg.append("XP: ").append(levelPlayer).append("\n");
            msg.append("Items: \n");
            msg.append(ItemsToString(playerItems)).append("\n");

            msg.append(partnerName).append("\n");
            msg.append("Money: ").append(moneyPartner).append("\n");
            msg.append("XP: ").append(levelPartner).append("\n");
            msg.append("Items: \n");
            msg.append(ItemsToString(partnerItems)).append("\n");
            msg.append("---------------------------------\n");

            SendLog(msg.toString());
        });
    }
}
