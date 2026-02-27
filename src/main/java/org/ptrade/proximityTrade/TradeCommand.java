package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
                return  true;
            }
            double distance = sender.getLocation().distance(receiver.getLocation());
            if(distance > MainConfig.maxTradeDistance){
                Helpers.SendFormated(sender, "&4You are too far away from  " + receiverName);
                return  true;
            }
        }


        if(receiver == sender){
            Helpers.SendFormated(sender,"You cannot offer trade proposal to yourself");
            return  false;
        }
        TradeStatus senderStatus = TradeList.GetStatus(sender.getUniqueId());
        TradeStatus receiverStatus = TradeList.GetStatus(receiver.getUniqueId());

        if(receiverStatus.trading){
            Helpers.SendFormated(sender,"This player is already trading with someone else");
            return  true;
        }

        Player reciverLastOffer = receiverStatus.GetLastOffer();
        if(reciverLastOffer != sender){
            receiverStatus.lastOffer = sender.getUniqueId();
            if(senderStatus.lastOffer != receiver.getUniqueId()){
                Helpers.SendFormated(sender, "Trade request send to: " + receiver.getName());
                Helpers.SendFormated(receiver,sender.getName() +
                        " wants to trade with you type /trade " + sender.getName() + " to accept");
                return true;
            }

        }
        if(senderStatus.lastOffer == receiver.getUniqueId()){
            senderStatus.trading = true;
            receiverStatus.trading = true;
            senderStatus.lastOffer = receiver.getUniqueId();
            receiverStatus.lastOffer = sender.getUniqueId();
            sender.openInventory(TradeGUI.Create(sender, receiver));
            receiver.openInventory(TradeGUI.Create(receiver, sender));
            return  true;
        }
        Helpers.SendFormated(sender,receiver.getName() + " already has your trade offer");
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
