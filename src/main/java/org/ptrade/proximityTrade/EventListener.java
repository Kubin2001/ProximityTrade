package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;


public class EventListener implements org.bukkit.event.Listener {
    public Plugin plugin;
    public EventListener(Plugin plugin){
        this.plugin = plugin;
    }

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
        if (event.getView().getTitle().equals("Trade Outcome")){
            TradeGUI.DropFinalInventory(p, event.getView());
            return;
        }
        TradeStatus status = TradeList.GetStatus(p.getUniqueId());

        if(status == null){
            return;
        }
        InventoryView view = event.getView();
        String title = view.getTitle();

        Player otherPlayer = status.GetLastOffer();
        if(otherPlayer == null){
            return;
        }

        if(title.equals("Trading with " + otherPlayer.getName())){
            status.Clear();
            TradeStatus otherStatus = TradeList.GetStatus(otherPlayer.getUniqueId());
            TradeGUI.DropInvItems(p, event.getInventory());

            if (otherStatus.GetLastOffer() != null && otherStatus.GetLastOffer().equals(p)) {
                otherStatus.Clear();
                if (otherPlayer.isOnline()) {
                    TradeGUI.DropInvItems(otherPlayer, otherPlayer.getOpenInventory().getTopInventory());
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
        Player otherPlayer = status.GetLastOffer();
        if(otherPlayer == null){
            status.Clear();
            return;
        }
        if(title.equals("Trading with " + otherPlayer.getName())){
            Inventory top = event.getView().getTopInventory();
            Inventory bottom = event.getView().getBottomInventory();
            Inventory clickedInv = event.getClickedInventory();
            if(event.getClickedInventory() != null && event.getClickedInventory().equals(top)){
                int slot = event.getSlot();

                boolean isRange1 = (slot >= 0 && slot <= 3);
                boolean isRange2 = (slot >= 9 && slot <= 12);
                boolean isRange3 = (slot >= 18 && slot <= 21);

                if(slot == 48){
                    if(TradeGUI.TryToFinalize(p,otherPlayer)){
                        TradeGUI.Finalize(p,otherPlayer);
                    }
                }

                if(TradeGUI.CheckIfConfirmed(p)){
                    event.setCancelled(true);
                }

                if(slot == 45){
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        TradeGUI.UpdatePartnerConfirmStatus(p, otherPlayer);
                    });
                    return;
                }

                if (!isRange1 && !isRange2 && !isRange3) {
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    TradeGUI.UpdateParterInv(p, otherPlayer);
                });
            }
            else if (clickedInv != null && clickedInv.equals(bottom) && event.isShiftClick()) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Ta metoda sprawdzi stan PO przeniesieniu
                    TradeGUI.UpdateParterInv(p, otherPlayer);
                });
            }
        }
    }
}
