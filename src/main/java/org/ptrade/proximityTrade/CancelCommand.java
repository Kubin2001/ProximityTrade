package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CancelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,  @NotNull String[] args){
        if(!(sender instanceof Player p)){
            Bukkit.getLogger().info("Command CancelTrade cannot be used by non player entity");
            return true;
        }
        TradeStatus playerStatus = TradeList.GetStatus(p.getUniqueId());
        Player target = playerStatus.GetLastOffer();
        if(target == null){
            Helpers.SendFormated(p, "&4You do not have any trade offer to cancel");
            Helpers.PlayNegativeSound(p);
        }
        else{
            playerStatus.lastOffer = null;
            Helpers.SendFormated(p, "&2Trade rejected");
            Helpers.SendFormated(target, "&4" +p.getName() + " has rejected your trade");
            Helpers.PlayNegativeSound(target);
            Helpers.PlayPositiveSound(p);

            TradeStatus targetStatus = TradeList.GetStatus(target.getUniqueId());
            if(targetStatus.GetLastOffer() == p){
                targetStatus.Clear();
            }
        }

        return true;
    }
}
