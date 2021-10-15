package fr.neyuux.refont.lg;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class GameLG implements Listener {

    public GameLG() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }

}
