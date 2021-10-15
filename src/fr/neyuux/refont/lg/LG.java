package fr.neyuux.refont.lg;

import org.bukkit.plugin.java.JavaPlugin;

public class LG extends JavaPlugin {

    private static LG INSTANCE;


    public static LG getInstance() {
        return INSTANCE;
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
