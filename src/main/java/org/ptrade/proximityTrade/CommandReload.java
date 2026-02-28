package org.ptrade.proximityTrade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandReload implements CommandExecutor {
    public Plugin plugin = null;

    public CommandReload(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        long timeStart = System.nanoTime ();
        MainConfig.Load(plugin);

        long timeEnd = System.nanoTime ();
        long lDurationSec = timeEnd - timeStart;

        double durationSec = lDurationSec / 1_000_000.0;
        if (sender instanceof Player p) {
            Helpers.SendFormated(p, "Restarted" + " time: " + durationSec + " ms");
        } else {
            plugin.getLogger().info("Restarted" + " time: " + durationSec + " ms");
        }
        return  true;
    }
}
