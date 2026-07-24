package org.ptrade.proximityTrade;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class MainConfig {
    public static int maxTradeDistance = 50;
    public static boolean ignoreWorlds = false;

    public static Sound positiveS = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
    public static Sound negativeS = Sound.BLOCK_NOTE_BLOCK_BASS;
    public static boolean enablePositiveSound = true;
    public static boolean enableNegativeSound = true;

    public static boolean logs = false;
    public  static  LogOutput logOutput = LogOutput.Console;

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
        File configF = new File(plugin.getDataFolder(), "config.yml");
        if(!configF.exists()){
            String fileName = "configBase.yml";
            if(Helpers.isPremium) {
                fileName = "configPremium.yml";
            }
            try(InputStream stream = plugin.getResource(fileName)){
                if(stream == null) {
                    plugin.getLogger().info("Error when streaming config please report this on plugin discord");
                    return;
                }
                Files.copy(stream, configF.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e){
                plugin.getLogger().info("Error when loading config please report this on plugin discord");
            }
        }

        plugin.reloadConfig();

        FileConfiguration config = plugin.getConfig();


        maxTradeDistance = config.getInt("maxDistance",50);
        ignoreWorlds = config.getBoolean("ignoreWorlds",false);

        positiveS = LoadSound(config.getString("PositiveSound","ENTITY_EXPERIENCE_ORB_PICKUP"),plugin);
        negativeS = LoadSound(config.getString("NegativeSound","BLOCK_NOTE_BLOCK_BASS"),plugin);

        enablePositiveSound = config.getBoolean ("EnablePositiveSounds", true);
        enableNegativeSound = config.getBoolean ("EnableNegativeSounds",true);

        if(!Helpers.isPremium){ return;}
        // TODO load premium settings
        logs = config.getBoolean("LogTrades", false);

        String logString = config.getString("LogType", "Console");
        if(logString.equals("File")){
            logOutput = LogOutput.File;
        }
        else{
            logOutput = LogOutput.Console;

        }

    }
}
