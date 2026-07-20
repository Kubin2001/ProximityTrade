package org.ptrade.proximityTrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TradeStatus {
    public UUID lastOffer = null;
    public boolean trading = false;
    public int xpVal = 0;
    public boolean confirmed = false;
    public  boolean finalized = false;
    public double money = 0.0;

    TradeStatus(Player p, boolean trading){
        if(p != null){
            lastOffer = p.getUniqueId();
        }
        this.trading = trading;
    }

    void Clear(){
        lastOffer = null;
        trading = false;
        xpVal = 0;
        money = 0.0;
        confirmed = false;
        finalized = false;
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
