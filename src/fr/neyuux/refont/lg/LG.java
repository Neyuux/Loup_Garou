package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.commands.LGCommand;
import fr.neyuux.refont.lg.items.ItemsManager;
import fr.neyuux.refont.lg.listeners.PlayerListener;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.*;
import fr.neyuux.refont.old.lg.commands.CommandAnkou;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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

    public static String getPlurial(int i) {
        if (i != 1) return "s";
        else return "";
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

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

        op.setPrefix("§5§lOP §c");
        op.setSuffix("§r");

        players.setDisplayName("Joueurs");
        players.setPrefix("§e");
        players.setSuffix("§r");

        villager.setDisplayName("Villageois-Villageois");
        villager.setPrefix("§a§lV§e-§a§lV §a");
        villager.setSuffix("§r");

        president.setDisplayName("Prédident");
        president.setPrefix("§e§lPrésident §6");
        president.setSuffix("§r");

        this.itemsManager = new ItemsManager();
        this.gameLG = new GameLG();
        this.initialiseRoles();

        this.getCommand("lg").setExecutor(new LGCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void initialiseRoles() {
        try {
            this.roles.put("Ancien", Ancien.class.getConstructor());
            this.roles.put("Ange", Ange.class.getConstructor());
            this.roles.put("Ankou", Ankou.class.getConstructor());
            this.roles.put("BoucEmissaire", BoucEmissaire.class.getConstructor());
            this.roles.put("Chaman", Chaman.class.getConstructor());
            this.roles.put("ChaperonRouge", ChaperonRouge.class.getConstructor());
            this.roles.put("Chasseur", Chasseur.class.getConstructor());
            this.roles.put("ChevalierALEpeeRouillee", ChevalierALEpeeRouillee.class.getConstructor());
            this.roles.put("ChienLoup", ChienLoup.class.getConstructor());
            this.roles.put("Comedien", Comedien.class.getConstructor());
            this.roles.put("Corbeau", Corbeau.class.getConstructor());
            this.roles.put("Cupidon", Cupidon.class.getConstructor());
            this.roles.put("Detective", Detective.class.getConstructor());
            this.roles.put("Dictateur", Dictateur.class.getConstructor());
            this.roles.put("DurACuire", DurACuire.class.getConstructor());
            this.roles.put("Enchanteur", Enchanteur.class.getConstructor());
            this.roles.put("EnfantSauvage", EnfantSauvage.class.getConstructor());
            this.roles.put("FilleDeJoie", FilleDeJoie.class.getConstructor());
            this.roles.put("Fossoyeur", Fossoyeur.class.getConstructor());
            this.roles.put("Frere", Frere.class.getConstructor());
            this.roles.put("GardeDuCorps", GardeDuCorps.class.getConstructor());
            this.roles.put("GrandMechantLoup", GrandMechantLoup.class.getConstructor());
            this.roles.put("HumainMaudit", HumainMaudit.class.getConstructor());
            this.roles.put("IdiotDuVillage", IdiotDuVillage.class.getConstructor());
            this.roles.put("InfectPereDesLoups", InfectPereDesLoups.class.getConstructor());
            this.roles.put("JoueurDeFlute", JoueurDeFlute.class.getConstructor());
            this.roles.put("Jumeau", Jumeau.class.getConstructor());
            this.roles.put("LoupGarou", LoupGarou.class.getConstructor());
            this.roles.put("LoupGarouBlanc", LoupGarouBlanc.class.getConstructor());
            this.roles.put("Macon", Macon.class.getConstructor());
            this.roles.put("MamieGrincheuse", MamieGrincheuse.class.getConstructor());
            this.roles.put("Mercenaire", Mercenaire.class.getConstructor());
            this.roles.put("MontreurDOurs", MontreurDOurs.class.getConstructor());
            this.roles.put("Necromancien", Necromancien.class.getConstructor());
            this.roles.put("Noctambule", Noctambule.class.getConstructor());
            this.roles.put("Pacifiste", Pacifiste.class.getConstructor());
            this.roles.put("PetiteFille", PetiteFille.class.getConstructor());
            this.roles.put("PetiteFilleWO", PetiteFilleWO.class.getConstructor());
            this.roles.put("PorteurDeLAmulette", PorteurDeLAmulette.class.getConstructor());
            this.roles.put("President", President.class.getConstructor());
            this.roles.put("Pretre", Pretre.class.getConstructor());
            this.roles.put("Pyromane", Pyromane.class.getConstructor());
            this.roles.put("Renard", Renard.class.getConstructor());
            this.roles.put("Salvateur", Salvateur.class.getConstructor());
            this.roles.put("ServanteDevouee", ServanteDevouee.class.getConstructor());
            this.roles.put("SimpleVillageois", SimpleVillageois.class.getConstructor());
            this.roles.put("Soeur", Soeur.class.getConstructor());
            this.roles.put("Sorciere", Sorciere.class.getConstructor());
            this.roles.put("VilainGarcon", VilainGarcon.class.getConstructor());
            this.roles.put("VillageoisVillageois", VillageoisVillageois.class.getConstructor());
            this.roles.put("Voleur", Voleur.class.getConstructor());
            this.roles.put("Voyante", Voyante.class.getConstructor());
            this.roles.put("VoyanteApprentie", VoyanteApprentie.class.getConstructor());
            this.roles.put("VoyanteDAura", VoyanteDAura.class.getConstructor());

        } catch (NoSuchMethodException|SecurityException e) {
            e.printStackTrace();
        }
    }



    public static void setPlayerInScoreboardTeam(String teamname, Player player) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamname);

        team.addEntry(player.getName());
        player.setDisplayName(team.getPrefix() + player.getName() + team.getSuffix());
        player.setPlayerListName(player.getDisplayName());
    }

    public static void removePlayerInScoreboardTeam(String teamname, Player player) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamname);

        team.removeEntry(player.getName());
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getDisplayName());
    }


    public GameLG getGame() {
        return this.gameLG;
    }

    public ItemsManager getItemsManager()  {
        return itemsManager;
    }

    public HashMap<String, Constructor<? extends Role>> getRoles() {
        return roles;
    }
}
