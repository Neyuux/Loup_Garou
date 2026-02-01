package fr.neyuux.lg;

import fr.minuskube.inv.SmartInvsPlugin;
import fr.neyuux.lg.commands.LGCommand;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.ResetEvent;
import fr.neyuux.lg.items.ItemsManager;
import fr.neyuux.lg.listeners.GameListener;
import fr.neyuux.lg.listeners.PreGameListener;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.classes.*;
import io.github.pr0methean.betterrandom.prng.adapter.SplittableRandomAdapter;
import io.github.pr0methean.betterrandom.seed.BufferedSeedGenerator;
import io.github.pr0methean.betterrandom.seed.DevRandomSeedGenerator;
import io.github.pr0methean.betterrandom.seed.SecureRandomSeedGenerator;
import io.github.pr0methean.betterrandom.seed.SeedGeneratorPreferenceList;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;

public class LG extends JavaPlugin {

    private static LG INSTANCE;

    @Getter
    private static final String prefix = "§c§lLoups§e§l-§6§lGarous §8§l» §r";

    public static final Random RANDOM = new SplittableRandomAdapter(new SeedGeneratorPreferenceList(false,
            new BufferedSeedGenerator(DevRandomSeedGenerator.DEV_RANDOM_SEED_GENERATOR, 128),
            SecureRandomSeedGenerator.DEFAULT_INSTANCE));


    private GameLG gameLG;

    @Getter
    private ItemsManager itemsManager;

    @Getter
    private final HashMap<String, Constructor<? extends Role>> roles = new HashMap<>();

    @Getter
    private final List<Listener> listeningEventsRoles = new ArrayList<>();

    public static LG getInstance() {
        return INSTANCE;
    }

    public static String getPlurial(int i) {
        if (i != 1) return "s";
        else return "";
    }

    public static String getStringBoolean(boolean b) {
        if (b) return "§aOui";
        else return "§cNon";
    }

    public static int adaptIntToInvSize(int i) {
        int newsize = Math.max(i, 5);

        if (i < 36) newsize = 4;
        if (i < 27) newsize = 3;
        if (i < 18) newsize = 2;
        if (i < 9) newsize = 1;

        return newsize;
    }

    public static byte translateChatColorToByte(ChatColor chatColor) {
        switch (chatColor) {
            case BLACK:
                return 15;

            case DARK_BLUE:
            case BLUE:
                return 11;

            case DARK_GREEN:
                return 13;

            case DARK_AQUA:
                return 9;

            case DARK_RED:
            case RED:
                return 14;

            case DARK_PURPLE:
                return 10;

            case GOLD:
                return 1;

            case GRAY:
                return 8;

            case DARK_GRAY:
                return 7;

            case GREEN:
                return 5;

            case AQUA:
                return 3;

            case LIGHT_PURPLE:
                return 6;

            case YELLOW:
                return 4;
        }
        return 0;
    }

    public static double distanceSquaredXZ(Location to, Location from) {
        return Math.pow(from.getX() - to.getX(), 2.0D) + Math.pow(from.getZ() - to.getZ(), 2.0D);
    }

    @Override
    public void onEnable() {
        if (System.getProperties().containsKey("RELOAD")) {
            if (System.getProperty("RELOAD").equals("TRUE"))
                return;
        } else {
            Properties prop = new Properties(System.getProperties());
            prop.put("RELOAD", "FALSE");
        }

        INSTANCE = this;

        Bukkit.getLogger().log(Level.INFO, "LG enabling");

        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            FileConfiguration config = getConfig();

            for (GameType gt : GameType.values())
                if (gt != GameType.NONE)
                    config.set("spawns." + gt, new ArrayList<>());

            saveConfig();
        }

        this.itemsManager = new ItemsManager();
        this.newGame();
        this.initialiseRoles();

        this.getCommand("lg").setExecutor(new LGCommand());
        this.getServer().getPluginManager().registerEvents(new PreGameListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "LG disabling");
        super.onDisable();
    }

    public void initialiseRoles() {
        this.roles.clear();
        try {
            this.roles.put("Ancien", Ancien.class.getConstructor());
            this.roles.put("Ange", Ange.class.getConstructor());
            this.roles.put("Ankou", Ankou.class.getConstructor());
            this.roles.put("Assassin", Assassin.class.getConstructor());
            this.roles.put("Bouffon", Bouffon.class.getConstructor());
            this.roles.put("Chaman", Chaman.class.getConstructor());
            this.roles.put("ChaperonRouge", ChaperonRouge.class.getConstructor());
            this.roles.put("Chasseur", Chasseur.class.getConstructor());
            this.roles.put("ChasseurDeVampire", ChasseurDeVampire.class.getConstructor());
            this.roles.put("ChevalierALEpeeRouillee", ChevalierALEpeeRouillee.class.getConstructor());
            this.roles.put("ChienLoup", ChienLoup.class.getConstructor());
            this.roles.put("Comedien", Comedien.class.getConstructor());
            this.roles.put("Corbeau", Corbeau.class.getConstructor());
            this.roles.put("Cupidon", Cupidon.class.getConstructor());
            this.roles.put("Detective", Detective.class.getConstructor());
            this.roles.put("Dictateur", Dictateur.class.getConstructor());
            this.roles.put("DurACuire", DurACuire.class.getConstructor());
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
            this.roles.put("Pirate", Pirate.class.getConstructor());
            this.roles.put("PorteurDeLAmulette", PorteurDeLAmulette.class.getConstructor());
            this.roles.put("President", President.class.getConstructor());
            this.roles.put("Pretre", Pretre.class.getConstructor());
            this.roles.put("Pronostiqueur", Pronostiqueur.class.getConstructor());
            this.roles.put("Pyromane", Pyromane.class.getConstructor());
            this.roles.put("Renard", Renard.class.getConstructor());
            this.roles.put("Salvateur", Salvateur.class.getConstructor());
            this.roles.put("ServanteDevouee", ServanteDevouee.class.getConstructor());
            this.roles.put("SimpleVillageois", SimpleVillageois.class.getConstructor());
            this.roles.put("Soeur", Soeur.class.getConstructor());
            this.roles.put("Sorciere", Sorciere.class.getConstructor());
            this.roles.put("Vampire", Vampire.class.getConstructor());
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

    public void unregisterAllListeners() {
        listeningEventsRoles.forEach(HandlerList::unregisterAll);
        listeningEventsRoles.clear();
    }

    public static void addNightVision(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
    }

    public static void addSaturation(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false));
    }

    public static void closeSmartInv(Player player) {
        SmartInvsPlugin.manager().getInventory(player).ifPresent(smartInventory -> smartInventory.close(player));
    }




    public GameLG getGame() {
        return this.gameLG;
    }

    public void newGame() {
        this.gameLG = new GameLG();

        Bukkit.getPluginManager().callEvent(new ResetEvent());

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLG.removePlayerLG(player);
            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

            player.setExp(0f);
            player.setLevel(0);
            player.setMaxHealth(20);
            player.setHealth(20);
            for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
            addNightVision(playerLG);
            addSaturation(playerLG);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(Bukkit.getWorld("LG"), LG.RANDOM.nextInt(16) + 120, 16.5, LG.RANDOM.nextInt(16) + 371));


            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            if (player.isOp()) this.gameLG.OP(playerLG);
            this.getItemsManager().updateSpawnItems(playerLG);

            playerLG.showAllPlayers();
        }

        Bukkit.createWorld(new WorldCreator("LG"));
        Bukkit.getWorld("LG").setTime(0);

        LG.getInstance().unregisterAllListeners();
        Bukkit.getScheduler().cancelTasks(this);

        this.gameLG.registerEvents(this.gameLG);

        gameLG.sendLobbySideScoreboardToAllPlayers();
        gameLG.updatePlayersScoreboard();
    }

}
