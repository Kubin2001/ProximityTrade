package org.ptrade.proximityTrade;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeList {
    private static Map<UUID, TradeStatus> statuses = new HashMap<>();

    public static TradeStatus GetStatus(UUID id){
        return  statuses.get(id);
    }

    public static void AddStatus(Player p){
        UUID id = p.getUniqueId();
        statuses.put(id, new TradeStatus(p,false));
    }

    public static void ClearStatus(Player p){
        statuses.remove(p.getUniqueId());
    }
}
