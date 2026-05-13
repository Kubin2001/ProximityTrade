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
    private static int GetExpFromLevel(int level) {
        if (level <= 16) {
            return (int) (Math.pow(level, 2) + (6 * level));
        } else if (level <= 31) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }
    }

    private static int GetTotalExperience(Player player) {
        int level = player.getLevel();
        int expFromLevel = GetExpFromLevel(level);
        int expInBar = Math.round(player.getExpToLevel() * player.getExp());
        return expFromLevel + expInBar;
    }

    public static void RemoveExperience(Player player, int amount) {
        int currentTotalXP = GetTotalExperience(player);
        int newXP = Math.max(0, currentTotalXP - amount);

        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.giveExp(newXP);
    }

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

        if(Helpers.isPremium){
            ItemStack xpDown =  Helpers.CreateItem(Material.EXPERIENCE_BOTTLE,
                                                        Helpers.CFormat("&4&lRemove Level"),"0");
            ItemStack xpUp =  Helpers.CreateItem(Material.EXPERIENCE_BOTTLE,
                                                   Helpers.CFormat("&2&lAdd Level"),"0");

            ItemStack levelInfoPartner =  Helpers.CreateItem(Material.EXPERIENCE_BOTTLE,
                                                      Helpers.CFormat("&6&lPartner Levels"),"0");

            inv.setItem(36, xpDown);
            inv.setItem(37, xpUp);

            inv.setItem(44, levelInfoPartner);
        }

        return  inv;
    }

    public static int GetPartnerSlot(int i){
        return i + 5;
    }

    public static void UpdatePartnerInv(Player player , Player partner){
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
        if(playerFinalItem == null){
            Bukkit.getLogger ().info ("Unexpected error in TradeGUI TryToFinalize playerFinalItem is null"
                                      + "please report this to plugin creator");
            return false;
        }
        if(playerFinalItem.getType() != Material.ENCHANTED_BOOK){
            playerFinalItem.setType(Material.ENCHANTED_BOOK);
            ItemStack partnerFinalItem = partnerTop.getItem(50);
            partnerFinalItem.setType(Material.ENCHANTED_BOOK);
            Helpers.PlayPositiveSound (player);
            Helpers.PlayPositiveSound (partner);

        }

        ItemStack partnerFinalItem = partnerTop.getItem(48);
        if(partnerFinalItem == null){
            return  false;
        }
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

        if(Helpers.isPremium){
            ItemStack playerXPRemoveItem = playerTop.getItem(36); // Xp for trade partner
            ItemStack partnerXPRemoveItem = partnerTop.getItem(36); // Xp for player
            if(playerXPRemoveItem != null  &&  partnerXPRemoveItem != null){
                int xpForPlayer = GetXPValue (partnerXPRemoveItem);
                int xpForPartner = GetXPValue (playerXPRemoveItem);
                if(xpForPlayer != 0){
                    int xpPointsForPlayer = GetExpFromLevel (xpForPlayer);
                    player.giveExp (xpPointsForPlayer);
                    RemoveExperience (partner,xpPointsForPlayer);
                    Helpers.SendFormated (player, "&2You received " + xpPointsForPlayer + " xp points");
                }
                if(xpForPartner != 0){
                    int xpPointsForPartner = GetExpFromLevel (xpForPartner);
                    partner.giveExp (xpPointsForPartner);
                    RemoveExperience (player, xpPointsForPartner);
                    Helpers.SendFormated (partner, "&2You received " + xpPointsForPartner + " xp points");
                }
            }
        }


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

        if (!partnerView.getTitle().equals("Trading with " + player.getName())){
            return;
        }

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack playerConfItem = playerTop.getItem(45);
        if(playerConfItem == null){
            Bukkit.getLogger ().info ("Unexpected error in TradeGUI TryToFinalize playerConfItem is null"
                                      + "please report this to plugin creator");
            return;
        }
        if(playerConfItem.getType() == Material.RED_CONCRETE){
            playerConfItem.setType(Material.LIME_CONCRETE);
            ItemMeta meta = playerConfItem.getItemMeta();
            List<String> metaList = new ArrayList<>();
            metaList.add("Confirmed");
            meta.setLore(metaList);
            playerConfItem.setItemMeta(meta);
        }
        else{
            return;
        }

        ItemStack partnerConfItem = partnerTop.getItem(53);
        if(partnerConfItem == null){
            Bukkit.getLogger ().info ("Unexpected error in TradeGUI TryToFinalize partnerConfItem is null"
                                      + "please report this to plugin creator");
            return;
        }
        if(partnerConfItem.getType() == Material.RED_CONCRETE){
            partnerConfItem.setType(Material.LIME_CONCRETE);
            ItemMeta meta = partnerConfItem.getItemMeta();
            List<String> metaList = new ArrayList<>();
            metaList = new ArrayList<>();
            metaList.add("Confirmed");
            meta.setLore(metaList);
            partnerConfItem.setItemMeta(meta);
            Helpers.PlayPositiveSound (player);
            Helpers.PlayPositiveSound (partner);
        }
    }

    public static void DropInvItems(Player player){
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

    private static int GetXPValue(ItemStack item){
        ItemMeta xpMeta = item.getItemMeta ();
        if(xpMeta == null){
            Bukkit.getLogger ().info ("Cannot get xp lore report this to server administrator");
            return 0;
        }
        List<String> lore = xpMeta.getLore ();
        if(lore == null){
            Bukkit.getLogger ().info ("Cannot get xp lore report this to server administrator");
            return 0;
        }
        String xpStr = lore.getFirst ();
        int xpVal = 0;
        try {
            xpVal = Integer.parseInt (xpStr);
        }
        catch (NumberFormatException ex){
            Bukkit.getLogger ().info ("Cannot convert lore to xp value report this to server administrator");
            return 0;
        }
        return xpVal;
    }

    private static boolean UpdateItemXpMeta(ItemStack item, int playerLevel, int change, boolean direct){
        if(item == null){
            Bukkit.getLogger ().info ("[ERROR] UpdateItemXpMeta got null item report this to plugin creator");
            return  false;
        }
        ItemMeta xpMeta = item.getItemMeta ();
        if(xpMeta == null){
            Bukkit.getLogger ().info ("Cannot update item xp lore report this to server administrator");
            return false;
        }
        List<String> lore = xpMeta.getLore ();
        if(lore == null){
            Bukkit.getLogger ().info ("Cannot update item xp lore report this to server administrator");
            return false;
        }
        if(lore.isEmpty ()){
            Bukkit.getLogger ().info ("Cannot update item xp lore report this to server administrator");
            return false;
        }
        String xpStr = lore.getFirst ();
        int xpVal = 0;
        try {
            xpVal = Integer.parseInt (xpStr);
        }
        catch (NumberFormatException ex){
            Bukkit.getLogger ().info ("Cannot update item xp lore report this to server administrator");
            return false;
        }

        xpVal += change;
        if(direct){
            lore.set(0, String.valueOf(xpVal));
            xpMeta.setLore(lore);
            item.setItemMeta(xpMeta);
            return  true;
        }



        if(change > 0 && playerLevel < xpVal){
            return false;
        }
        else if (change < 0 && xpVal < 0){
            return  false;
        }

        lore.set(0, String.valueOf(xpVal));
        xpMeta.setLore(lore);
        item.setItemMeta(xpMeta);

        return true;
    }

    public static boolean ModifyXp(Player player , Player partner, int val){
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName())){
            return false;
        }

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack playerXPRemoveItem = playerTop.getItem(36); // Bootle remove xp
        ItemStack playerXPAddItem = playerTop.getItem(37); // Bootle add xp

        if(!UpdateItemXpMeta (playerXPAddItem, player.getLevel (),val,false)){
            return false;
        }
        if(!UpdateItemXpMeta (playerXPRemoveItem, player.getLevel (),val,false)){
            return false;
        }

        return true;
    }

    public static void UpdatePartnerXp(Player partner, int change){
        InventoryView partnerView = partner.getOpenInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack item = partnerTop.getItem(44);

        UpdateItemXpMeta(item, partner.getLevel(), change,true);
    }

}
