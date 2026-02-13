package org.ptrade.proximityTrade;

import org.bukkit.entity.Player;

public class TradeStatus {
    public Player lastOffer = null;
    public boolean trading = false;

    TradeStatus(Player p, boolean trading){
        lastOffer = null;
        this.trading = trading;
    }
}
