package org.ptrade.proximityTrade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class EventListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        TradeList.AddStatus(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();
        TradeList.ClearStatus(p);
    }
}
