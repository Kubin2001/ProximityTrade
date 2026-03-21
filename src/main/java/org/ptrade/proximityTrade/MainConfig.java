package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;


public class MainConfig {
    public static int maxTradeDistance = 50;
    public static boolean ignoreWorlds = false;

    public static Sound positiveS = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
    public static Sound negativeS = Sound.BLOCK_NOTE_BLOCK_BASS;
    public static boolean enablePossitiveSound = true;
    public static boolean enableNegativeSound = true;

    private static Sound LoadSound(String name, Plugin p){
        String strUpper = name.toUpperCase();
        // Replacing Spaces with _
        if(strUpper.contains(" ")){
            strUpper = strUpper.replace(" ", "_");
        }
        // Replacing Dots with _
        if(strUpper.contains(".")){
            strUpper = strUpper.replace(".", "_");
        }
        try{
            return Sound.valueOf(strUpper);
        }
        catch (Exception e){
            p.getLogger().info("Cannot load sound from config is it missing? Or maybe format is wrong");
        }
        return null;
    }

    public static void Load(Plugin plugin){
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig ();
        maxTradeDistance = config.getInt("maxDistance",50);
        ignoreWorlds = config.getBoolean("ignoreWorlds",false);

        positiveS = LoadSound(config.getString("PositiveSound","ENTITY_EXPERIENCE_ORB_PICKUP"),plugin);
        negativeS = LoadSound(config.getString("NegativeSound","BLOCK_NOTE_BLOCK_BASS"),plugin);

        enablePossitiveSound = config.getBoolean ("EnablePositiveSounds",true);
        enableNegativeSound = config.getBoolean ("EnableNegativeSounds",true);

    }
}
