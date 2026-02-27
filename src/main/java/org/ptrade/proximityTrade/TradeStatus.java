package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TradeStatus {
    public UUID lastOffer = null;
    public boolean trading = false;

    TradeStatus(Player p, boolean trading){
        if(p != null){
            lastOffer = p.getUniqueId();
        }
        this.trading = trading;
    }

    void Clear(){
        lastOffer = null;
        trading = false;
    }

    Player GetLastOffer(){
        if(lastOffer == null){
            return  null;
        }
        Player p =  Bukkit.getPlayer(lastOffer);
        if(p != null && p.isOnline()){
            return p;
        }
        return  null;
    }
}
