package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.config.GameConfig;
import fr.neyuux.refont.lg.items.ItemsManager;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.LoupGarouBlanc;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GameLG implements Listener {

    private GameConfig config;

    private GameState gameState = GameState.WAITING;

    private GameType gameType = GameType.NONE;

    private DayCycle dayCycle = DayCycle.NONE;

    private int day = 0;

    private int night = 0;

    private PlayerLG mayor;

    private final ArrayList<Role> rolesAtStart = new ArrayList<>();

    private final ArrayList<Role> aliveRoles = new ArrayList<>();

    private final ArrayList<PlayerLG> playersInGame = new ArrayList<>();

    private final ArrayList<PlayerLG> spectators = new ArrayList<>();

    private final ArrayList<PlayerLG> opList = new ArrayList<>();


    public GameLG() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
        this.config = new GameConfig(this);
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
        if (!opList.contains(playerLG)) this.opList.add(playerLG);
        playerLG.getPlayer().getInventory().setItem(6, new OpComparatorItemStack());
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AOP").addEntry(playerLG.getName());
    }

    public void unOP(PlayerLG playerLG) {
        this.opList.remove(playerLG);
        playerLG.getPlayer().getInventory().remove(new OpComparatorItemStack());
    }

    public void setSpectator(PlayerLG playerLG) {
        Player player = playerLG.getPlayer();

        this.spectators.add(playerLG);

        this.getItemsManager().updateSpawnItems(playerLG);
        player.setGameMode(GameMode.SPECTATOR);
        player.setDisplayName("§8[§7Spectateur§8]" + player.getName());
        player.setPlayerListName(player.getDisplayName());
        player.sendMessage(this.getPrefix() + "§9Votre mode de jeu a été établi en §7spectateur§9.");
        player.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/spec off§c.");
        //TODO update all scoreboard (less players)
    }

    public void addNightVision(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
    }

    public void addSaturation(PlayerLG playerLG) {
        playerLG.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false));
    }

    public void resetGame() {
        this.playersInGame.clear();
        this.spectators.clear();
        this.day = 0;
        this.night = 0;
        this.mayor = null;
        this.aliveRoles.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

            player.getInventory().clear();
            player.setExp(0f);
            player.setLevel(0);
            player.setMaxHealth(20);
            player.setHealth(20);
            for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
            this.addNightVision(playerLG);
            this.addSaturation(playerLG);
            player.setGameMode(GameMode.ADVENTURE);
            //TODO tp config locations.mainSpawn
            //player.teleport(p.teleport(new Location(Bukkit.getWorld("LG"), 494, 12.2, 307, 0f, 0f)); //494 12 307)

            this.getItemsManager().updateSpawnItems(playerLG);

            //TODO update scoreboard
            PlayerLG.removePlayerLG(player);
            PlayerLG.createPlayerLG(player);
        }

        Bukkit.getWorld("LG").setTime(0);

        this.setGameState(GameState.WAITING);
        this.setGameType(GameType.NONE);
        this.setDayCycle(DayCycle.NONE);
        Bukkit.getScheduler().cancelTasks(LG.getInstance());

        //TODO update all scoreboards
    }

    public List<PlayerLG> getOPs() {
        return this.opList;
    }

    public List<PlayerLG> getSpectators() {
        return this.spectators;
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

    public ArrayList<Role> getRolesAtStart() {
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
        sendTitleToAllPlayers(gameType.getName(), "§fa été choisi comme nouveau type de jeu !" , 20, 60, 20);
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
}