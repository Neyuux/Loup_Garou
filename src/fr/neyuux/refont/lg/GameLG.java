package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.config.GameConfig;
import fr.neyuux.refont.lg.event.TryStartGameEvent;
import fr.neyuux.refont.lg.items.ItemsManager;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.LoupGarouBlanc;
import fr.neyuux.refont.lg.roles.classes.Voleur;
import fr.neyuux.refont.lg.tasks.GameRunnable;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GameLG implements Listener {

    private GameConfig config;

    private GameState gameState = GameState.WAITING;

    private GameType gameType = GameType.NONE;

    private DayCycle dayCycle = DayCycle.NONE;

    private int day = 0;

    private int night = 0;

    private PlayerLG mayor;
    
    private int waitTicks = 0;

    private BukkitTask waitTask;

    private ArrayList<Constructor<? extends Role>> rolesAtStart = new ArrayList<>();

    private ArrayList<PlayerLG> waitedPlayers = new ArrayList<>();

    private final ArrayList<Role> aliveRoles = new ArrayList<>();

    private final ArrayList<PlayerLG> playersInGame = new ArrayList<>();

    private final ArrayList<PlayerLG> spectators = new ArrayList<>();

    private final ArrayList<HumanEntity> opList = new ArrayList<>();



    public void createGame() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
        this.config = new GameConfig(this);

        this.resetGame();
    }

    public void sendMessage(Role role, String msg) {
        for (PlayerLG playerLG : this.getAlive()) {
            if (role != null) {
                if (role.getPlayers().contains(playerLG))
                    playerLG.sendMessage(msg);
            } else playerLG.sendMessage(msg);
        }
    }

    public void sendActionBar(String msg) {
        for (PlayerLG playerLG : this.playersInGame) {
            playerLG.sendActionBar(msg);
        }
    }

    public List<PlayerLG> getAlive() {
        ArrayList<PlayerLG> alive = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (playerLG.isDead())
                alive.add(playerLG);

        return alive;
    }

    public List<PlayerLG> getLGs() {
        ArrayList<PlayerLG> lgs = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (playerLG.getCamp().equals(Camps.LOUP_GAROU)
                || playerLG.getRole().getClass().equals(LoupGarouBlanc.class))
                lgs.add(playerLG);

        return lgs;
    }

    public void OP(PlayerLG playerLG) {
        if (!opList.contains(playerLG.getPlayer())) this.opList.add(playerLG.getPlayer());
        playerLG.getPlayer().getInventory().setItem(6, new OpComparatorItemStack());
        LG.setPlayerInScoreboardTeam("AOP", playerLG.getPlayer());
    }

    public void unOP(PlayerLG playerLG) {
        this.opList.remove(playerLG.getPlayer());
        playerLG.getPlayer().getInventory().remove(new OpComparatorItemStack());
        LG.setPlayerInScoreboardTeam("Players", playerLG.getPlayer());
    }

    public void setSpectator(PlayerLG playerLG) {
        Player player = playerLG.getPlayer();

        this.spectators.add(playerLG);
        this.playersInGame.remove(playerLG);

        this.getItemsManager().updateSpawnItems(playerLG);
        player.setGameMode(GameMode.SPECTATOR);
        player.setDisplayName("§8[§7Spectateur§8]" + player.getName());
        player.setPlayerListName(player.getDisplayName());
        player.sendMessage(this.getPrefix() + "§9Votre mode de jeu a été établi en §7spectateur§9.");
        player.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/lg spec off§c.");
        //TODO update all scoreboard (less players)
    }

    public void removeSpectator(PlayerLG playerLG) {
        Player player = playerLG.getPlayer();

        this.spectators.remove(playerLG);

        this.getItemsManager().updateSpawnItems(playerLG);
        player.setMaxHealth(20);
        player.setHealth(20);

        for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
        this.addSaturation(playerLG);
        this.addNightVision(playerLG);

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        player.sendMessage(this.getPrefix() + "§9Vous avez été retiré du mode Spectateur.");
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(Bukkit.getWorld("LG"), new Random().nextInt(16) + 120, 16.5, new Random().nextInt(16) + 371));

        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Players").addEntry(player.getName());
    }

    public boolean joinGame(PlayerLG playerLG) {
        if (this.gameType.equals(GameType.FREE)) {
            playerLG.getPlayer().teleport(new Location(Bukkit.getWorld("LG"), 363.5, 84, 635.5, 180f, 0f));
        } else if (this.gameType.equals(GameType.MEETING)) {
            playerLG.getPlayer().teleport(new Location(Bukkit.getWorld("LG"), 102.5, 30.5, 847, -157.6f, 0f));
        } else if (this.gameType.equals(GameType.NONE)) {
            playerLG.sendMessage(LG.getPrefix() + "§cVous devez attendre qu'un OP choisisse le type de jeu (Libre ou Réunion) !");
            playNegativeSound(playerLG.getPlayer());
            return false;
        }

        if (!playersInGame.contains(playerLG)) this.playersInGame.add(playerLG);

        LG.getInstance().getItemsManager().updateSpawnItems(playerLG);

        playerLG.sendTitle("§5§ka §4§ka §c§ka §a§lVous jouerez cette partie ! §6§ka §e§ka §f§ka ", "§6Il y a désormais §e" + this.playersInGame.size() + "§6 joueur"+ LG.getPlurial(this.playersInGame.size()) +".", 20, 60, 20);
        playPositiveSound(playerLG.getPlayer());

        TryStartGameEvent ev = new TryStartGameEvent();
        Bukkit.getPluginManager().callEvent(ev);
        //TODO updateScoreboard

        return true;
    }

    public void leaveGame(PlayerLG playerLG) {
        if (playersInGame.contains(playerLG)) this.playersInGame.remove(playerLG);

        LG.getInstance().getItemsManager().updateSpawnItems(playerLG);
        playerLG.getPlayer().teleport(new Location(Bukkit.getWorld("LG"), new Random().nextInt(16) + 120, 16.5, new Random().nextInt(16) + 371));
        playerLG.sendTitle("§c§lVous avez été retiré de la partie !", "§6Pas de pot.", 20, 60, 20);
        playNegativeSound(playerLG.getPlayer());
        //TODO updateScoreboard
    }

    public void addNightVision(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
    }

    public void addSaturation(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false));
    }

    public static void playNegativeSound(Player player) {
        player.playSound(player.getLocation(), Sound.DOOR_CLOSE, 8, 2);
    }

    public static void playPositiveSound(Player player) {
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 8f, 1.8f);
    }

    public void start() {
        LG.getInstance().getGame().setGameState(GameState.PLAYING);
        this.rolesAtStart = new ArrayList<>(this.config.getAddedRoles());

        Bukkit.broadcastMessage(LG.getPrefix() + "§eLancement du jeu !");
        GameLG.sendTitleToAllPlayers("§b§lGO !", "", 20, 20, 20);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
            p.setTotalExperience(0);
            p.setGameMode(GameMode.ADVENTURE);

            LG.setPlayerInScoreboardTeam("Players", p);
        }

        BukkitTask deal = this.dealRoles();

        new GameRunnable(deal).runTaskTimer(LG.getInstance(), 0, 20);
    }

    public BukkitTask dealRoles() {
       this.waitedPlayers = (ArrayList<PlayerLG>) this.playersInGame.clone();
        ArrayList<Role> toGive = new ArrayList<>();
        Random random = new Random();
        try {
            Constructor<? extends Role> thiefConstructor = LG.getInstance().getRoles().get("Voleur");

            if (this.config.getAddedRoles().contains(thiefConstructor)) {
                Voleur thief = (Voleur) thiefConstructor.newInstance();

                Role role1 = this.config.getAddedRoles().get(random.nextInt(this.config.getAddedRoles().size())).newInstance();
                while (role1.getConfigName().equals("Voleur")) role1 = this.config.getAddedRoles().get(random.nextInt(this.config.getAddedRoles().size())).newInstance();

                Role role2 = this.config.getAddedRoles().get(random.nextInt(this.config.getAddedRoles().size())).newInstance();
                while (role2.getConfigName().equals("Voleur")) role2 = this.config.getAddedRoles().get(random.nextInt(this.config.getAddedRoles().size())).newInstance();

                this.config.getAddedRoles().remove(role1.getClass().getConstructor());
                this.config.getAddedRoles().remove(role2.getClass().getConstructor());

                thief.role1 = role1;
                thief.role2 = role2;

                System.out.println("rolevoleur : " + role1.getConfigName());
                System.out.println("rolevoleur : " + role2.getConfigName());
            }

            for (Constructor<? extends Role> constructor : this.config.getAddedRoles())
                toGive.add(constructor.newInstance());

            return Bukkit.getScheduler().runTaskTimer(LG.getInstance(), () -> {
                PlayerLG playerLG = this.waitedPlayers.get(random.nextInt(this.waitedPlayers.size()));
                Role role;

                if (playerLG.getRole() != null) {
                    System.out.println("forced " + playerLG.getName());
                    role = playerLG.getRole();
                    toGive.remove(role);

                } else role = toGive.remove(random.nextInt(toGive.size()));


                GameLG.sendActionBarToAllPlayers(LG.getPrefix() + "§eDealing role to §b" + playerLG.getName());

                playerLG.joinRole(role);
                playerLG.setCamp(role.getBaseCamp());
                playerLG.createScoreboard();
                LG.getInstance().getItemsManager().updateStartItems(playerLG);
                this.aliveRoles.add(role);
            }, 0, 13);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cImpossible de distribuer les rôles. Veuillez prévenir Neyuux_ ou réessayer.");
        }
        return null;
    }

    public void wait(final int seconds, final Runnable callback, final StringTimerMessage actionBarMessage) {
        cancelWait();
        this.waitTicks = seconds * 20;
        this.waitTask = (new BukkitRunnable() {
            public void run() {
                for (PlayerLG playerLG : playersInGame) {
                    Player player = playerLG.getPlayer();

                    player.setLevel(Math.floorDiv(GameLG.this.waitTicks, 20) + 1);
                    player.setExp(GameLG.this.waitTicks / (seconds * 20.0F));
                    playerLG.sendActionBar(actionBarMessage.generate(playerLG, Math.floorDiv(GameLG.this.waitTicks, 20) + 1));
                }

                if (GameLG.this.waitTicks == 0) {
                    GameLG.this.waitTask = null;
                    cancel();
                    callback.run();
                }
                GameLG.this.waitTicks--;
            }
        }).runTaskTimer(LG.getInstance(), 0L, 1L);
    }

    public void cancelWait() {
        if (this.waitTask != null) {
            this.waitTask.cancel();
            this.waitTask = null;
        }
    }

    public void resetGame() {
        this.setGameState(GameState.WAITING);
        this.setGameType(GameType.NONE);
        this.setDayCycle(DayCycle.NONE);

        this.playersInGame.clear();
        this.waitedPlayers.clear();
        this.spectators.clear();
        this.day = 0;
        this.night = 0;
        this.mayor = null;
        this.aliveRoles.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

            player.setExp(0f);
            player.setLevel(0);
            player.setMaxHealth(20);
            player.setHealth(20);
            for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
            this.addNightVision(playerLG);
            this.addSaturation(playerLG);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(Bukkit.getWorld("LG"), new Random().nextInt(16) + 120, 16.5, new Random().nextInt(16) + 371));

            PlayerLG.removePlayerLG(player);
            PlayerLG.createPlayerLG(player);

            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            LG.setPlayerInScoreboardTeam("Players", player);
            if (player.isOp()) this.OP(playerLG);
            this.getItemsManager().updateSpawnItems(playerLG);

            //List<Object> spawnObjects = new ArrayList<>((List<?>) LG.getInstance().getConfig().get("spawns." + this.gameType));
            //List<Double> location = (List<Double>)spawnObjects.remove(new Random().nextInt(spawnObjects.size()));

            //playerLG.setPlacement(new Location(Bukkit.getWorld("LG"), location.get(0) + 0.5, location.get(1), location.get(2) + 0.5, location.get(3).floatValue(), location.get(4).floatValue()));
        }

        Bukkit.createWorld(new WorldCreator("LG"));
        Bukkit.getWorld("LG").setTime(0);

        Bukkit.getScheduler().cancelTasks(LG.getInstance());

        this.setConfig(new GameConfig(this));

        //TODO update all scoreboards
    }

    public List<PlayerLG> getPlayersByRole(String configname) {
        List<PlayerLG> players = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (playerLG.getRole().getConfigName().equals(configname)) {
                players.add(playerLG);
            }

        return players;
    }

    public List<HumanEntity> getOPs() {
        return this.opList;
    }

    public List<PlayerLG> getSpectators() {
        return this.spectators;
    }

    public List<PlayerLG> getPlayersInGame() {
        return this.playersInGame;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public DayCycle getDayCycle() {
        return dayCycle;
    }

    public void setDayCycle(DayCycle dayCycle) {
        this.dayCycle = dayCycle;
    }

    public int getDay() {
        return day;
    }

    public void addDay() {
        this.day += 1;
    }

    public int getNight() {
        return night;
    }

    public void addNight() {
        this.night += 1;
    }

    public PlayerLG getMayor() {
        return mayor;
    }

    public void setMayor(PlayerLG mayor) {
        this.mayor = mayor;
    }

    public ArrayList<Constructor<? extends Role>> getRolesAtStart() {
        return rolesAtStart;
    }

    public ArrayList<Role> getAliveRoles() {
        return aliveRoles;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        if (gameType != GameType.NONE) sendTitleToAllPlayers(gameType.getName(), "§fa été choisi comme nouveau type de jeu !" , 20, 60, 20);
    }

    public ItemsManager getItemsManager() {
        return LG.getInstance().getItemsManager();
    }

    public String getPrefix() {
        return LG.getPrefix();
    }

    public static void sendTitleToAllPlayers(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        for (PlayerLG playerLG : PlayerLG.getPlayersMap().values())
            playerLG.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
    }

    public static void sendActionBarToAllPlayers(String msg) {
        for (PlayerLG playerLG : PlayerLG.getPlayersMap().values())
            playerLG.sendActionBar(msg);
    }

    public GameConfig getConfig() {
        return config;
    }

    private void setConfig(GameConfig config) {
        this.config = config;
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public int getWaitTicksToSeconds() {
        return Math.floorDiv(this.waitTicks, 20) + 1;
    }

    public void setWaitTicks(int waitTicks) {
        this.waitTicks = waitTicks;
    }

    public BukkitTask getWaitTask() {
        return waitTask;
    }

    public void setWaitTask(BukkitTask waitTask) {
        this.waitTask = waitTask;
    }

    public interface StringTimerMessage {
        String generate(PlayerLG param1LGPlayer, int param1Int);
    }
}