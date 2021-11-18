package fr.neyuux.refont.lg;

import org.bukkit.plugin.java.JavaPlugin;

public class LG extends JavaPlugin {

    private static LG INSTANCE;

    private static final String prefix = "§c§lLoups§e§l-§6§lGarous";


    public static LG getInstance() {
        return INSTANCE;
    }

    public static String getPrefix() {
        return prefix;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
