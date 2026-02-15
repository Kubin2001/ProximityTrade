package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeGUI {
    public static Inventory Create(Player p, Player target){
        Inventory inv =  Bukkit.createInventory(p,54, "Trading with " + target.getName());
        // Vertical Line
        inv.setItem(4,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(13,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(22,  new ItemStack(Material.BLACK_STAINED_GLASS));

        // Horizontal Line
        inv.setItem(27,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(28,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(29,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(30,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(31,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(32,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(33,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(34,  new ItemStack(Material.BLACK_STAINED_GLASS));
        inv.setItem(35,  new ItemStack(Material.BLACK_STAINED_GLASS));

        return  inv;
    }
}
