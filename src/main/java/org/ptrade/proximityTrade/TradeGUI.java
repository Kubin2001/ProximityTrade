package org.ptrade.proximityTrade;

import net.milkbowl.vault.economy.Economy;
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

        // Economy trading money
        if(Helpers.isPremium && Helpers.hasEconomy){
            ItemStack moneyDown =  Helpers.CreateItem(Material.GOLD_NUGGET,
                                                   Helpers.CFormat("&4&lRemove Money"),"0");
            ItemStack moneyUp =  Helpers.CreateItem(Material.GOLD_NUGGET,
                                                 Helpers.CFormat("&2&lAdd Money"),"0");

            ItemStack moneyInfoPartner =  Helpers.CreateItem(Material.GOLD_NUGGET,
                                                             Helpers.CFormat("&6&lPartner Money"),"0");
            inv.setItem(38, moneyDown);
            inv.setItem(39, moneyUp);

            inv.setItem(43, moneyInfoPartner);
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
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        TradeStatus partnerStatus = TradeList.GetStatus (partner.getUniqueId ());
        if(playerStatus == null || partnerStatus == null){ return false;}

        InventoryView playerView = player.getOpenInventory();
        InventoryView partnerView = partner.getOpenInventory();

        if(!playerStatus.confirmed || !partnerStatus.confirmed){
            return  false;
        }
        Inventory playerTop = playerView.getTopInventory();
        ItemStack playerFinalItem = playerTop.getItem(48);
        if(!UpdateItem (playerFinalItem, Material.ENCHANTED_BOOK,null,null )){
            return  false;
        }
        playerStatus.finalized = true;
        Helpers.PlayPositiveSound (player);
        Helpers.PlayPositiveSound (partner);
        // Update partner info if player has finalized
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack partnerFinalItem = partnerTop.getItem(50);
        if(!UpdateItem (partnerFinalItem, Material.ENCHANTED_BOOK,null,null )){
            return  false;
        }
        if(playerStatus.finalized && partnerStatus.finalized){
            return true;
        }
        return false;
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
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        TradeStatus partnerStatus = TradeList.GetStatus (partner.getUniqueId ());
        if(playerStatus == null || partnerStatus == null){
            Bukkit.getLogger ().info ("Unexpected error player or partner has no status");
            return;
        }
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        Inventory playerTop = playerView.getTopInventory();
        Inventory partnerTop = partnerView.getTopInventory();

        if(Helpers.isPremium){
            ItemStack playerXPRemoveItem = playerTop.getItem(36); // Xp for trade partner
            ItemStack partnerXPRemoveItem = partnerTop.getItem(36); // Xp for player
            if(playerXPRemoveItem != null  &&  partnerXPRemoveItem != null){
                int xpForPlayer = partnerStatus.xpVal;
                int xpForPartner = playerStatus.xpVal;
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

        if (Helpers.isPremium && Helpers.hasEconomy) {
            Economy eco = Helpers.ecoHook.getEconomy();
            double moneyForPartner = playerStatus.money;
            double moneyForPlayer = partnerStatus.money;
            double playerBalance = eco.getBalance(player);
            double partnerBalance = eco.getBalance(partner);

            if (moneyForPartner > 0.0 && playerBalance >= moneyForPartner) {
                eco.withdrawPlayer(player, moneyForPartner);
                eco.depositPlayer(partner, moneyForPartner);
                Helpers.SendFormated (partner, "&2You received " + moneyForPartner + " coins");
            }

            if (moneyForPlayer > 0.0 && partnerBalance >= moneyForPlayer) {
                eco.withdrawPlayer(partner, moneyForPlayer);
                eco.depositPlayer(player, moneyForPlayer);
                Helpers.SendFormated (player, "&2You received " + moneyForPartner + " coins");
            }
        }

        ArrayList<ItemStack> playerItems = GetInvItems(partnerTop);
        ArrayList<ItemStack> partnerItems = GetInvItems(playerTop);

        if(Helpers.isPremium && MainConfig.logs){
            // Player and partner items are swapped
            LogPack lp = new LogPack(player, partner, playerStatus,partnerStatus, partnerItems, playerItems);
            lp.Log();
        }

        partnerTop.clear();
        playerTop.clear();

        playerStatus.Clear();
        partnerStatus.Clear();

        Inventory playerInv = Bukkit.createInventory(player,54, "Trade Outcome");
        for(int i = 0; i < playerItems.size(); i++){
            playerInv.setItem(i,playerItems.get(i));
        }
        player.openInventory(playerInv);
        Helpers.PlayPositiveSound (player);

        Inventory partnerInv = Bukkit.createInventory(partner,54, "Trade Outcome");
        for(int i = 0; i < partnerItems.size(); i++){
            partnerInv.setItem(i,partnerItems.get(i));
        }
        partner.openInventory(partnerInv);
        Helpers.PlayPositiveSound (partner);

    }

    public static boolean CheckIfConfirmed(Player player){
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        if(playerStatus == null){
            return  false;
        }
        return  playerStatus.confirmed;
    }

    private static boolean UpdateItem(ItemStack item, Material newMaterial, String newName, String newLore){
        if(item == null){
            return  false;
        }
        if(newMaterial != null){
            item.setType (newMaterial);
        }
        ItemMeta meta = item.getItemMeta ();
        if(meta == null){
            return  false;
        }
        if(newName != null){
            meta.setDisplayName (newName);
        }

        item.setItemMeta (meta);
        if(newLore != null){
            List<String> lore = new ArrayList<> ();
            lore.add (newLore);
            meta.setLore (lore);
        }
        item.setItemMeta (meta);

        return true;
    }

    // Called when player click confirm item it updates its status and view for partner
    public static void UpdatePartnerConfirmStatus(Player player , Player partner){
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        TradeStatus partnerStatus = TradeList.GetStatus (partner.getUniqueId ());
        if(playerStatus == null || partnerStatus == null){
            Bukkit.getLogger ().info ("Unexpected error player or partner has no status");
            return;
        }
        if(playerStatus.confirmed){ // If it is already confirmed no need to check anything
            return;
        }

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
            UpdateItem (playerConfItem, Material.LIME_CONCRETE, null, "Confirmed");
            playerStatus.confirmed = true;
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
            UpdateItem (partnerConfItem, Material.LIME_CONCRETE, null, "Confirmed");
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

    private static boolean UpdateItemXpMeta(ItemStack item, int playerLevel, int newVal, boolean force){
        if(force){
            if(!UpdateItem (item,null,null,String.valueOf (newVal))){
                Bukkit.getLogger ().info ("[ERROR] UpdateItemXpMeta cannot update item");
                return  false;
            }
            return  true;
        }

        if(playerLevel < newVal || newVal < 0){
            return false;
        }
        if(!UpdateItem (item,null,null,String.valueOf (newVal))){
            Bukkit.getLogger ().info ("[ERROR] UpdateItemXpMeta cannot update item");
            return  false;
        }
        return true;
    }


    public static boolean ModifyXp(Player player , Player partner, int val){
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        TradeStatus partnerStatus = TradeList.GetStatus (partner.getUniqueId ());
        if(playerStatus.confirmed){
            return  false;
        }
        if(playerStatus == null || partnerStatus == null){
            Bukkit.getLogger ().info ("Unexpected error player or partner has no status");
            return false;
        }
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName())){
            return false;
        }

        Inventory playerTop = playerView.getTopInventory();
        ItemStack playerXPRemoveItem = playerTop.getItem(36); // Bottle remove xp
        ItemStack playerXPAddItem = playerTop.getItem(37); // Bottle add xp

        int newVal = playerStatus.xpVal + val;
        if(!UpdateItemXpMeta (playerXPAddItem, player.getLevel (),newVal,false)){
            return false;
        }
        if(!UpdateItemXpMeta (playerXPRemoveItem, player.getLevel (),newVal,false)){
            return false;
        }
        playerStatus.xpVal = newVal;
        return true;
    }

    public static boolean ModifyMoney(Player player , Player partner, int val){
        TradeStatus playerStatus = TradeList.GetStatus (player.getUniqueId ());
        TradeStatus partnerStatus = TradeList.GetStatus (partner.getUniqueId ());
        if(playerStatus.confirmed){
            return false;
        }
        if(playerStatus == null || partnerStatus == null){
            Bukkit.getLogger ().info ("Unexpected error player or partner has no status");
            return false;
        }
        InventoryView partnerView = partner.getOpenInventory();
        InventoryView playerView = player.getOpenInventory();

        if (!partnerView.getTitle().equals("Trading with " + player.getName())){
            return false;
        }

        Inventory playerTop = playerView.getTopInventory();
        ItemStack playerMoneyRemoveItem = playerTop.getItem(38); // Nugger remove money
        ItemStack playerMoneyAddItemItem = playerTop.getItem(39); // Nugget add money

        double currentVal = playerStatus.money;
        double balance = Helpers.ecoHook.getEconomy ().getBalance (player);
        double newVal = currentVal + val;
        if(newVal > balance || newVal < 0){
            return  false;
        }
        if(!UpdateItem (playerMoneyRemoveItem,null,null,String.valueOf ((int)newVal))){
            Bukkit.getLogger ().info ("[ERROR] UpdateItemXpMeta cannot update item");
            return  false;
        }
        if(!UpdateItem (playerMoneyAddItemItem,null,null,String.valueOf ((int)newVal))){
            Bukkit.getLogger ().info ("[ERROR] UpdateItemXpMeta cannot update item");
            return  false;
        }
        playerStatus.money = newVal;
        return true;
    }

    public static void UpdatePartnerXp(Player partner, int val){
        InventoryView partnerView = partner.getOpenInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack item = partnerTop.getItem(44);

        UpdateItemXpMeta(item, partner.getLevel(), val,true);
    }

    public static void UpdatePartnerMoney(Player partner, double val){
        InventoryView partnerView = partner.getOpenInventory();
        Inventory partnerTop = partnerView.getTopInventory();
        ItemStack item = partnerTop.getItem(43);

        UpdateItem (item,null,null,String.valueOf ((int)val));
    }
}
