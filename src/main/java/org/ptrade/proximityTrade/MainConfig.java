package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    // Premium Values
    public static boolean logs = false;
    public static LogOutput logOutput = LogOutput.Console;

    public static Material borderMaterial = Material.GRAY_STAINED_GLASS_PANE;

    public static Material nonConfirmMaterial = Material.RED_CONCRETE;
    public static Material confirmMaterial = Material.LIME_CONCRETE;
    public static Material nonFinalizeMaterial = Material.WRITABLE_BOOK;
    public static Material finalizeMaterial = Material.ENCHANTED_BOOK;

    public static Material addXPMaterial = Material.EXPERIENCE_BOTTLE;
    public static Material removeXPMaterial = Material.EXPERIENCE_BOTTLE;
    public static Material partnerXPMaterial = Material.EXPERIENCE_BOTTLE;

    public static Material addMoneyMaterial = Material.GOLD_NUGGET;
    public static Material removeMoneyMaterial = Material.GOLD_NUGGET;
    public static Material partnerMoneyMaterial = Material.GOLD_NUGGET;

    private static Material ParseMaterial(String name){
        if(name == null){
            return  null;
        }
        String strUpper = name.toUpperCase().replace(" ", "_").replace(".", "_");
        try{
            return Material.valueOf(strUpper);
        }
        catch (Exception e){
            Bukkit.getLogger().info("Cannot load material: " + name + " from config is it missing? Or maybe format is wrong");
        }
        return  null;
    }

    private static Sound LoadSound(String name){
        String strUpper = name.toUpperCase().replace(" ", "_").replace(".", "_");
        try{
            return Sound.valueOf(strUpper);
        }
        catch (Exception e){
            Bukkit.getLogger().info("Cannot load sound from config is it missing? Or maybe format is wrong");
        }
        return null;
    }

    private static Material FillMaterial(Material defaultMat, FileConfiguration config, String name){
        String matString = config.getString(name, null);
        Material material = ParseMaterial(matString);
        if(material == null){
            return defaultMat;
        }
        return material;
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

        positiveS = LoadSound(config.getString("PositiveSound","ENTITY_EXPERIENCE_ORB_PICKUP"));
        negativeS = LoadSound(config.getString("NegativeSound","BLOCK_NOTE_BLOCK_BASS"));

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

        borderMaterial = FillMaterial(borderMaterial, config, "BorderMaterial");

        nonConfirmMaterial= FillMaterial(nonConfirmMaterial, config, "NonConfirmMaterial");
        confirmMaterial = FillMaterial(confirmMaterial, config, "ConfirmMaterial");
        nonFinalizeMaterial = FillMaterial(nonFinalizeMaterial, config, "NonFinalizeMaterial");
        finalizeMaterial = FillMaterial(finalizeMaterial, config, "FinalizeMaterial");

        addXPMaterial= FillMaterial(addXPMaterial, config, "AddXPMaterial");
        removeXPMaterial = FillMaterial(removeXPMaterial, config, "RemoveXPMaterial");
        partnerXPMaterial = FillMaterial(partnerXPMaterial, config, "PartnerXPMaterial");

        addMoneyMaterial = FillMaterial(addMoneyMaterial, config, "AddMoneyMaterial");
        removeMoneyMaterial = FillMaterial(removeMoneyMaterial, config, "RemoveMoneyMaterial");
        partnerMoneyMaterial = FillMaterial(partnerMoneyMaterial, config, "PartnerMoneyMaterial");


    }
}
