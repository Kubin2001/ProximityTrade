package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        ItemStack finalizeItem =  Helpers.CreateItem(Material.WRITABLE_BOOK,
                Helpers.CFormat("&2&lFinalize"),"");

        inv.setItem(48, finalizeItem);

        ItemStack finalizePartnerItem =  Helpers.CreateItem(Material.WRITABLE_BOOK,
                Helpers.CFormat("&2&lFinalize Partner"),"");
        inv.setItem(50, finalizePartnerItem);

        ItemStack confPlayer =  Helpers.CreateItem(Material.RED_CONCRETE, Helpers.CFormat("&2&lConfirmation"),
                "Not confirmed");

        inv.setItem(45, confPlayer);

        ItemStack confPartner =  Helpers.CreateItem(Material.RED_CONCRETE,
                Helpers.CFormat("&2&lTrade Partner Confirmation"), "Not confirmed");

        inv.setItem(53, confPartner);

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

    public static boolean TryToFinalize(Player player , Player partner){
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName())){
            return false;
        }

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();

        if(!CheckIfConfirmed(player) || !CheckIfConfirmed(partner)){
            return false;
        }

        ItemStack playerFinalItem = playerTop.getItem(48);
        if(playerFinalItem.getType() != Material.ENCHANTED_BOOK){
            playerFinalItem.setType(Material.ENCHANTED_BOOK);
            ItemStack patnerFinalItem = partnerTop.getItem(50);
            patnerFinalItem.setType(Material.ENCHANTED_BOOK);

        }

        ItemStack partnerFinalItem = partnerTop.getItem(48);
        if(partnerFinalItem.getType() != Material.ENCHANTED_BOOK){
            return false;
        }
        else{
            return true;
        }
    }

    private static ArrayList<ItemStack> GetInvItems(Inventory inv){
        ArrayList<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < 4 ; i ++){
            ItemStack item = inv.getItem(i);
            if(item == null || item.getType() == Material.AIR){
                continue;
            }
            items.add(item.clone());
        }
        for (int i = 9; i < 13 ; i ++){
            ItemStack item = inv.getItem(i);
            if(item == null || item.getType() == Material.AIR){
                continue;
            }
            items.add(item.clone());
        }
        for (int i = 18; i < 22 ; i ++){
            ItemStack item = inv.getItem(i);
            if(item == null || item.getType() == Material.AIR){
                continue;
            }
            items.add(item.clone());
        }
        return  items;
    }

    public static void Finalize(Player player , Player partner){
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();

        ArrayList<ItemStack> playerItems = GetInvItems(partnerTop);
        ArrayList<ItemStack> partnerItems = GetInvItems(playerTop);

        partnerTop.clear();
        playerTop.clear();

        TradeStatus playerStatus = TradeList.GetStatus(player.getUniqueId());
        TradeStatus partnerStatus = TradeList.GetStatus(partner.getUniqueId());
        playerStatus.Clear();
        partnerStatus.Clear();

        Inventory playerInv = Bukkit.createInventory(player,54, "Trade Outcome");
        for(int i = 0; i < playerItems.size(); i++){
            playerInv.setItem(i,playerItems.get(i));
        }
        player.openInventory(playerInv);

        Inventory partnerInv = Bukkit.createInventory(partner,54, "Trade Outcome");
        for(int i = 0; i < partnerItems.size(); i++){
            partnerInv.setItem(i,partnerItems.get(i));
        }
        partner.openInventory(partnerInv);

    }

    public static boolean CheckIfConfirmed(Player player){
        InventoryView playerView = player.getOpenInventory();
        Inventory playerTop = playerView.getTopInventory();
        ItemStack confirmItem = playerTop.getItem(45);
        if (confirmItem == null || confirmItem.getType().isAir()){
            return false;
        }

        if(confirmItem.getType() == Material.LIME_CONCRETE){
            return  true;
        }
        return  false;
    }

    public static void UpdatePartnerConfirmStatus(Player player , Player partner){
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName()))
            return;

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack item = playerTop.getItem(45);
        if(item.getType() == Material.RED_CONCRETE){
            item.setType(Material.LIME_CONCRETE);
            ItemMeta meta = item.getItemMeta();
            List<String> metaList = new ArrayList<>();
            metaList.add("Confirmed");
            meta.setLore(metaList);
            item.setItemMeta(meta);
            item =  partnerTop.getItem(53);
            if(item.getType() == Material.RED_CONCRETE){
                item.setType(Material.LIME_CONCRETE);
                meta = item.getItemMeta();
                metaList = new ArrayList<>();
                metaList.add("Confirmed");
                meta.setLore(metaList);
                item.setItemMeta(meta);
            }
        }
    }

    public static void DropInvItems(Player player, Inventory inv){
        InventoryView playerView = player.getOpenInventory();
        Inventory playerTop = playerView.getTopInventory();
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 4 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            items.add(item);
        }
        for (int i = 9; i < 13 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            items.add(item);
        }
        for (int i = 18; i < 22 ; i ++){
            ItemStack item = playerTop.getItem(i);
            if(item == null){
                continue;
            }
            items.add(item);
        }

        for (ItemStack item : items) {

            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);

            for (ItemStack rest : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), rest);
            }
        }
    }

    public static void DropFinalInventory(Player player, InventoryView view) {
        Inventory inventory = view.getTopInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);

            if (item == null || item.getType().isAir())
                continue;

            player.getWorld().dropItemNaturally(player.getLocation(),item);
        }
        inventory.clear();
    }
}
