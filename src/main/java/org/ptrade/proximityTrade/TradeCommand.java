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
    public boolean onCommand(CommandSender sen, Command command, String label, String[] args){
        if(!(sen instanceof Player sender)){return false;}

        if(args.length != 1){return false;}

        String receiverName = args[0];

        Player receiver = Bukkit.getPlayer(receiverName);

        if(receiver == null){
            return  false;
        }
        if(receiver == sender){
            Helpers.SendFormated(sender,"You cannot offer trade proposal to yourself");
            return  false;
        }
        TradeStatus senderStatus = TradeList.GetStatus(sender.getUniqueId());
        TradeStatus reciverStatus = TradeList.GetStatus(receiver.getUniqueId());

        if(reciverStatus.trading){
            Helpers.SendFormated(sender,"This player is already trading with someone else");
            return  true;
        }
        if(reciverStatus.lastOffer != sender){
            Helpers.SendFormated(receiver,sender.getName() + " wants to trade with you type /trade " + sender.getName() + " to accept");
            reciverStatus.lastOffer = sender;
            senderStatus.lastOffer = receiver;
            return true;
        }
        if(senderStatus.lastOffer == receiver){
            senderStatus.trading = true;
            reciverStatus.trading = true;
            senderStatus.lastOffer = receiver;
            reciverStatus.lastOffer = sender;
            sender.openInventory(TradeGUI.Create(sender, receiver));
            receiver.openInventory(TradeGUI.Create(receiver, sender));
            return  true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (command.getName()) {
            case "trade":
                return GetPlayerNames(args);
            default:
                return Collections.emptyList();
        }
    }

}
