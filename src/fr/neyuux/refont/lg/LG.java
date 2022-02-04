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
        this.initialiseRoles();
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
            this.roles.put("Comedien", Comedien.class.getConstructor(GameLG.class));
            this.roles.put("Corbeau", Corbeau.class.getConstructor(GameLG.class));
            this.roles.put("Cupidon", Cupidon.class.getConstructor(GameLG.class));
            this.roles.put("Detective", Detective.class.getConstructor(GameLG.class));
            this.roles.put("Dictateur", Dictateur.class.getConstructor(GameLG.class));
            this.roles.put("DurACuire", DurACuire.class.getConstructor(GameLG.class));
            this.roles.put("Enchanteur", Enchanteur.class.getConstructor(GameLG.class));
            this.roles.put("EnfantSauvage", EnfantSauvage.class.getConstructor(GameLG.class));
            this.roles.put("FilleDeJoie", FilleDeJoie.class.getConstructor(GameLG.class));
            this.roles.put("Fossoyeur", Fossoyeur.class.getConstructor(GameLG.class));
            this.roles.put("Frere", Frere.class.getConstructor(GameLG.class));
            this.roles.put("GardeDuCorps", GardeDuCorps.class.getConstructor(GameLG.class));
            this.roles.put("GrandMechantLoup", GrandMechantLoup.class.getConstructor(GameLG.class));
            this.roles.put("HumainMaudit", HumainMaudit.class.getConstructor(GameLG.class));
            this.roles.put("IdiotDuVillage", IdiotDuVillage.class.getConstructor(GameLG.class));
            this.roles.put("InfectPereDesLoups", InfectPereDesLoups.class.getConstructor(GameLG.class));
            this.roles.put("JoueurDeFlute", JoueurDeFlute.class.getConstructor(GameLG.class));
            this.roles.put("Jumeau", Jumeau.class.getConstructor(GameLG.class));
            this.roles.put("LoupGarou", LoupGarou.class.getConstructor(GameLG.class));
            this.roles.put("LoupGarouBlanc", LoupGarouBlanc.class.getConstructor(GameLG.class));
            this.roles.put("Macon", Macon.class.getConstructor(GameLG.class));
            this.roles.put("MamieGrincheuse", MamieGrincheuse.class.getConstructor(GameLG.class));
            this.roles.put("Mercenaire", Mercenaire.class.getConstructor(GameLG.class));
            this.roles.put("MontreurDOurs", MontreurDOurs.class.getConstructor(GameLG.class));
            this.roles.put("Necromancien", Necromancien.class.getConstructor(GameLG.class));
            this.roles.put("Noctambule", Noctambule.class.getConstructor(GameLG.class));
            this.roles.put("Pacifiste", Pacifiste.class.getConstructor(GameLG.class));
            this.roles.put("PetiteFille", PetiteFille.class.getConstructor(GameLG.class));
            this.roles.put("PetiteFilleWO", PetiteFilleWO.class.getConstructor(GameLG.class));
            this.roles.put("PorteurDeLAmulette", PorteurDeLAmulette.class.getConstructor(GameLG.class));
            this.roles.put("President", President.class.getConstructor(GameLG.class));
            this.roles.put("Pretre", Pretre.class.getConstructor(GameLG.class));
            this.roles.put("Pyromane", Pyromane.class.getConstructor(GameLG.class));
            this.roles.put("Renard", Renard.class.getConstructor(GameLG.class));
            this.roles.put("Salvateur", Salvateur.class.getConstructor(GameLG.class));
            this.roles.put("ServanteDevouee", ServanteDevouee.class.getConstructor(GameLG.class));
            this.roles.put("SimpleVillageois", SimpleVillageois.class.getConstructor(GameLG.class));
            this.roles.put("Soeur", Soeur.class.getConstructor(GameLG.class));
            this.roles.put("Sorciere", Sorciere.class.getConstructor(GameLG.class));
            this.roles.put("VilainGarcon", VilainGarcon.class.getConstructor(GameLG.class));
            this.roles.put("VillageoisVillageois", VillageoisVillageois.class.getConstructor(GameLG.class));
            this.roles.put("Voleur", Voleur.class.getConstructor(GameLG.class));
            this.roles.put("Voyante", Voyante.class.getConstructor(GameLG.class));
            this.roles.put("VoyanteApprentie", VoyanteApprentie.class.getConstructor(GameLG.class));
            this.roles.put("VoyanteDAura", VoyanteDAura.class.getConstructor(GameLG.class));

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
