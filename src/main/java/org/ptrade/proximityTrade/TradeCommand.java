package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeCommand implements CommandExecutor, TabCompleter {
    public List<String> GetPlayerNames(String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                String name = p.getName();
                if (name.toLowerCase().startsWith(prefix)) {
                    names.add(name);
                }
            }
            Collections.sort(names);
            return names;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player p)){return false;}

        if(args.length != 1){return false;}

        String receiverName = args[0];

        Player receiver = Bukkit.getPlayer(receiverName);

        if(receiver == null){
            return  false;
        }
        receiver.sendMessage(p.getName() + " wants to trade with you");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (command.getName()) {
            case "setRewardLevel":
            case "resetRewardTimer":
            case "rewardInfo":
            case "moveReward":
                return GetPlayerNames(args);
            default:
                return Collections.emptyList();
        }
    }

}
