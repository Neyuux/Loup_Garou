package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.items.ItemsManager;
import fr.neyuux.refont.lg.listeners.PlayerListener;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class LG extends JavaPlugin {

    private static LG INSTANCE;

    private static final String prefix = "§c§lLoups§e§l-§6§lGarous §8§l» §r";


    private GameLG gameLG;

    private ItemsManager itemsManager;

    private final HashMap<String, Constructor<? extends Role>> roles = new HashMap<>();

    public static LG getInstance() {
        return INSTANCE;
    }

    public static String getPrefix() {
        return prefix;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.gameLG = new GameLG();
        this.itemsManager = new ItemsManager(gameLG);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        if (!new File(getDataFolder(), "config.yml").exists()) {
            FileConfiguration config = this.getConfig();

            config.set("locations", new ArrayList<>());

            saveConfig();
        }

        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

        for (Team t : s.getTeams())
            if (!t.getDisplayName().startsWith("Kits"))
                t.unregister();

        for (Objective ob : s.getObjectives())
            ob.unregister();

        Team op = s.registerNewTeam("AOP");
        Team players = s.registerNewTeam("Players");
        Team villager = s.registerNewTeam("RVillageois");
        Team president = s.registerNewTeam("RPresident");

        op.setPrefix("§c§lLeader §f§l");
        op.setSuffix("§d§k§laa§r");

        players.setDisplayName("Joueurs");
        players.setPrefix("§e");
        players.setSuffix("§r");

        villager.setDisplayName("Villageois-Villageois");
        villager.setPrefix("§a§lV§e-§a§lV §a");
        villager.setSuffix("§r");

        president.setDisplayName("Prédident");
        president.setPrefix("§e§lPrésident §6");
        president.setSuffix("§r");

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void initialiseRoles() {
        try {
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ange", Ange.class.getConstructor(GameLG.class));
            this.roles.put("Ankou", Ankou.class.getConstructor(GameLG.class));
            this.roles.put("BoucEmissaire", BoucEmissaire.class.getConstructor(GameLG.class));
            this.roles.put("Chaman", Chaman.class.getConstructor(GameLG.class));
            this.roles.put("ChaperonRouge", ChaperonRouge.class.getConstructor(GameLG.class));
            this.roles.put("Chasseur", Chasseur.class.getConstructor(GameLG.class));
            this.roles.put("ChevalierALEpeeRouillee", ChevalierALEpeeRouillee.class.getConstructor(GameLG.class));
            this.roles.put("ChienLoup", ChienLoup.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));
            this.roles.put("Ancien", Ancien.class.getConstructor(GameLG.class));

        } catch (NoSuchMethodException|SecurityException e) {
            e.printStackTrace();
        }
    }


    public GameLG getGame() {
        return gameLG;
    }

    public ItemsManager getItemsManager()  {
        return itemsManager;
    }

    public HashMap<String, Constructor<? extends Role>> getRoles() {
        return roles;
    }
}
