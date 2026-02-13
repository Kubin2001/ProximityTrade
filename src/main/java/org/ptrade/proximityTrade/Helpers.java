package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helpers {
    private static Random rand = null;

    public static void Init() {
        rand = new Random();
    }

    public static String CFormat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void SendFormated(Player p, String s) {
        p.sendMessage(Helpers.CFormat(s));
    }

    public static int GetRandom(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static void PlaySoundToPLayer(Player p, Sound sound) {
        p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
    }

    public static void RunTask(Plugin plugin, Runnable task, int delay) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    public static void RepairAll(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable dmg) {
                dmg.setDamage(0);
                item.setItemMeta((ItemMeta) dmg);
            }
        }
    }

    public static ItemStack CreateItem(Material mat, String name, String desc) {
        ItemStack retItem = new ItemStack(mat);

        ItemMeta meta = retItem.getItemMeta();
        meta.setDisplayName(Helpers.CFormat(name));
        List<String> lore = new ArrayList<>();
        lore.add(Helpers.CFormat(desc));
        meta.setLore(lore);
        retItem.setItemMeta(meta);
        return retItem;
    }
}
