package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TradeGUI {
    public static Inventory Create(Player p, Player target){
        Inventory inv =  Bukkit.createInventory(p,54, "Trading with " + target.getName());
        // Vertical Line
        Material glassMat = Material.GRAY_STAINED_GLASS_PANE;
        inv.setItem(4,  new ItemStack(glassMat));
        inv.setItem(13,  new ItemStack(glassMat));
        inv.setItem(22,  new ItemStack(glassMat));

        // Horizontal Line
        inv.setItem(27,  new ItemStack(glassMat));
        inv.setItem(28,  new ItemStack(glassMat));
        inv.setItem(29,  new ItemStack(glassMat));
        inv.setItem(30,  new ItemStack(glassMat));
        inv.setItem(31,  new ItemStack(glassMat));
        inv.setItem(32,  new ItemStack(glassMat));
        inv.setItem(33,  new ItemStack(glassMat));
        inv.setItem(34,  new ItemStack(glassMat));
        inv.setItem(35,  new ItemStack(glassMat));

        inv.setItem(45, new ItemStack(Material.WRITABLE_BOOK));

        inv.setItem(47, new ItemStack(Material.RED_CONCRETE));

        inv.setItem(51, new ItemStack(Material.RED_CONCRETE));

        return  inv;
    }

    public static int GetPartnerSlot(int i){
        return i + 5;
    }

    public static void UpdateParterInv(Player player , Player partner){
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName()))
            return;

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        for (int i = 0; i < 4 ; i ++){
            ItemStack item = playerTop.getItem(i);
            partnerTop.setItem(GetPartnerSlot(i),item);
        }
        for (int i = 9; i < 13 ; i ++){
            ItemStack item = playerTop.getItem(i);
            partnerTop.setItem(GetPartnerSlot(i),item);
        }
        for (int i = 18; i < 22 ; i ++){
            ItemStack item = playerTop.getItem(i);
            partnerTop.setItem(GetPartnerSlot(i),item);
        }
    }

    public static void DropInvItems(Player player, Inventory inv){
        InventoryView playerView = player.getOpenInventory();
        Inventory playerTop = playerView.getTopInventory();
        for (int i = 0; i < 4 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
        for (int i = 9; i < 13 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
        for (int i = 18; i < 22 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

}
