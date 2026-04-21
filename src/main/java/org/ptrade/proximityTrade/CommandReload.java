package org.ptrade.proximityTrade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandReload implements CommandExecutor {
    public Plugin plugin;

    public CommandReload(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args){
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
