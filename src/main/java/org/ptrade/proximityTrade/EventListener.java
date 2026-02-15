package org.ptrade.proximityTrade;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;


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

    @EventHandler
    public void onInvClose(InventoryCloseEvent event){
        Player p = (Player)event.getPlayer();
        TradeStatus status = TradeList.GetStatus(p.getUniqueId());
        InventoryView view = event.getView();
        String title = view.getTitle();
        Player otherPlayer = status.lastOffer;
        if(otherPlayer == null){
            return;
        }
        if(title.equals("Trading with " + otherPlayer.getName())){
            status.Clear();
            TradeStatus otherStatus = TradeList.GetStatus(otherPlayer.getUniqueId());
            if (otherStatus.lastOffer != null && otherStatus.lastOffer.equals(p)) {
                otherStatus.Clear();
                if (otherPlayer.isOnline()) {
                    otherPlayer.closeInventory();
                    Helpers.SendFormated(otherPlayer, p.getName() + " canceled Trade Deal");
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        String title = event.getView().getTitle();
        Player p = (Player)event.getWhoClicked();
        TradeStatus status = TradeList.GetStatus(p.getUniqueId());
        Player otherPlayer = status.lastOffer;
        if(otherPlayer == null){
            status.Clear();
            return;
        }
        if(title.equals("Trading with " + otherPlayer.getName())){
            int slot = event.getRawSlot();
            boolean isRange1 = (slot >= 0 && slot <= 3);
            boolean isRange2 = (slot >= 9 && slot <= 12);
            boolean isRange3 = (slot >= 18 && slot <= 21);
            if (!isRange1 && !isRange2 && !isRange3) {
                event.setCancelled(true);
            }
        }
    }
}
