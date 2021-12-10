package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class LG extends JavaPlugin {

    private static LG INSTANCE;

    private static final String prefix = "§c§lLoups§e§l-§6§lGarous";


    private GameLG gameLG;

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

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

        for (Team t : s.getTeams()) {
            if (!t.getDisplayName().startsWith("Kits")) {
                t.unregister();
            }
        }
        for (Objective ob : s.getObjectives()) {
            ob.unregister();
        }

        String tabplace = "A";
        String st = "";
        String color = "§f";
        s.registerNewTeam(tabplace + "ADieu" + st);
        s.getTeam(tabplace + "ADieu" + st).setDisplayName("Dieu" + st);
        s.getTeam(tabplace + "ADieu" + st).setPrefix("§c§lDieu. " + color + "§l");
        s.getTeam(tabplace + "ADieu" + st).setSuffix("§d§k§laa§r");

        s.registerNewTeam(tabplace + "BDieuM" + st);
        s.getTeam(tabplace + "BDieuM" + st).setDisplayName("DieuM" + st);
        s.getTeam(tabplace + "BDieuM" + st).setPrefix("§5§lDieu. " + color + "§l");
        s.getTeam(tabplace + "BDieuM" + st).setSuffix("§6§k§laa§r");

        s.registerNewTeam(tabplace + "CDieuX" + st);
        s.getTeam(tabplace + "CDieuX" + st).setDisplayName("DieuX" + st);
        s.getTeam(tabplace + "CDieuX" + st).setPrefix("§6§lDieu. " + color + "§l");
        s.getTeam(tabplace + "CDieuX" + st).setSuffix("§5§k§laa§r");

        s.registerNewTeam(tabplace + "DDieuE" + st);
        s.getTeam(tabplace + "DDieuE" + st).setDisplayName("DieuE" + st);
        s.getTeam(tabplace + "DDieuE" + st).setPrefix("§3§lDieu. " + color + "§l");
        s.getTeam(tabplace + "DDieuE" + st).setSuffix("§0§k§laa§r");

        s.registerNewTeam(tabplace + "EDémon" + st);
        s.getTeam(tabplace + "EDémon" + st).setDisplayName("Démon" + st);
        s.getTeam(tabplace + "EDémon" + st).setPrefix("§b§lDémon. " + color + "§l");
        s.getTeam(tabplace + "EDémon" + st).setSuffix("§c§k§laa§r");

        s.registerNewTeam(tabplace + "FLeader" + st);
        s.getTeam(tabplace + "FLeader" + st).setDisplayName("Leader" + st);
        s.getTeam(tabplace + "FLeader" + st).setPrefix("§2Leader. " + color + "§l");
        s.getTeam(tabplace + "FLeader" + st).setSuffix("§0§kaa§r");

        s.registerNewTeam("AGJoueur");
        s.getTeam("AGJoueur").setDisplayName("Joueur");
        s.getTeam("AGJoueur").setPrefix("§e");
        s.getTeam("AGJoueur").setSuffix("§r");

        s.registerNewTeam("RVillageois");
        s.getTeam("RVillageois").setDisplayName("Villageois-Villageois");
        s.getTeam("RVillageois").setPrefix("§a§lV§e-§a§lV §a");
        s.getTeam("RVillageois").setSuffix("§r");

        s.registerNewTeam("RPrésident");
        s.getTeam("RPrésident").setDisplayName("Président");
        s.getTeam("RPrésident").setPrefix("§e§lPrésident §6");
        s.getTeam("RPrésident").setSuffix("§r");

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public GameLG getGame() {
        return gameLG;
    }
}
