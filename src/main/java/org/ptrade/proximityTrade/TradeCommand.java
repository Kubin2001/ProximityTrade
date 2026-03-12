package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeCommand implements CommandExecutor, TabCompleter {

    public List<String> GetPlayerNames(String[] args, String playerName) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(p.getName().equals(playerName)){
                    continue;
                }
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
    public boolean onCommand(CommandSender sen, Command command, String label, String[] args){
        if(!(sen instanceof Player sender)){return false;}

        if(args.length != 1){return false;}

        String receiverName = args[0];

        Player receiver = Bukkit.getPlayer(receiverName);

        if(receiver == null){
            return  false;
        }

        if(!MainConfig.ignoreWorlds){
            if(receiver.getWorld() != sender.getWorld()){
                Helpers.SendFormated(sender, "&4You cannot send trade proposal when " + receiverName +
                        " is not in the same world");
                Helpers.PlayNegativeSound (sender);
                return  true;
            }
            double distance = sender.getLocation().distance(receiver.getLocation());
            if(distance > MainConfig.maxTradeDistance){
                Helpers.SendFormated(sender, "&4You are too far away from  " + receiverName);
                Helpers.PlayNegativeSound (sender);
                return  true;
            }
        }


        if(receiver == sender){
            Helpers.SendFormated(sender,"&4You cannot offer trade proposal to yourself");
            Helpers.PlayNegativeSound (sender);
            return  false;
        }
        TradeStatus senderStatus = TradeList.GetStatus(sender.getUniqueId());
        TradeStatus receiverStatus = TradeList.GetStatus(receiver.getUniqueId());

        if(receiverStatus.trading){
            Helpers.SendFormated(sender,"&4This player is already trading with someone else");
            Helpers.PlayNegativeSound (sender);
            return  true;
        }

        Player receiverLastOffer = receiverStatus.GetLastOffer();
        if(receiverLastOffer != sender){
            receiverStatus.lastOffer = sender.getUniqueId();
            if(senderStatus.lastOffer != receiver.getUniqueId()){
                Helpers.SendFormated(sender, "&2Trade request send to: " + receiver.getName());
                Helpers.SendFormated(receiver,"&2" + sender.getName() +
                        " wants to trade with you type /trade " + sender.getName() + " to accept");
                Helpers.PlayPositiveSound (sender);
                Helpers.PlayPositiveSound (receiver);
                return true;
            }

        }
        if(senderStatus.lastOffer == receiver.getUniqueId()){
            senderStatus.trading = true;
            receiverStatus.trading = true;
            senderStatus.lastOffer = receiver.getUniqueId();
            receiverStatus.lastOffer = sender.getUniqueId();
            Helpers.PlayPositiveSound (sender);
            Helpers.PlayPositiveSound (receiver);
            sender.openInventory(TradeGUI.Create(sender, receiver));
            receiver.openInventory(TradeGUI.Create(receiver, sender));
            return  true;
        }
        Helpers.SendFormated(sender,"&2" + receiver.getName() + " already has your trade offer");
        Helpers.PlayNegativeSound (sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equals("trade")){
            return GetPlayerNames(args, sender.getName());
        }
        return Collections.emptyList();
    }

}
