package fr.neyuux.lg;

import fr.neyuux.lg.config.GameConfig;
import fr.neyuux.lg.config.MayorSuccession;
import fr.neyuux.lg.enums.DayCycle;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.enums.WinCamps;
import fr.neyuux.lg.event.TryStartGameEvent;
import fr.neyuux.lg.event.WinEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.items.ItemsManager;
import fr.neyuux.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.classes.*;
import fr.neyuux.lg.tasks.GameRunnable;
import fr.neyuux.lg.tasks.LGStop;
import fr.neyuux.lg.utils.CacheLG;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameLG implements Listener {

    private final GameConfig config;

    private GameState gameState = GameState.WAITING;

    private GameType gameType = GameType.NONE;

    private DayCycle dayCycle = DayCycle.NONE;

    private int day = 0;

    private int night = 0;

    private PlayerLG mayor;
    
    private int waitTicks = 0;

    private BukkitTask waitTask;

    private GameRunnable gameRunnable;

    private VoteLG vote;

    private final ArrayList<Role> rolesAtStart = new ArrayList<>();

    private ArrayList<PlayerLG> waitedPlayers = new ArrayList<>();

    private final ArrayList<Role> aliveRoles = new ArrayList<>();

    private final ArrayList<PlayerLG> playersInGame = new ArrayList<>();

    private final ArrayList<PlayerLG> spectators = new ArrayList<>();

    private final ArrayList<HumanEntity> opList = new ArrayList<>();

    private final ArrayList<PlayerLG> killedPlayers = new ArrayList<>();


    public GameLG() {
        this.config = new GameConfig();
    }


    public void sendMessage(Class<? extends Role> role, String msg) {
        for (PlayerLG playerLG : this.getAlive()) {
            if (role != null) {
                if (this.getPlayersByRole(role).contains(playerLG))
                    playerLG.sendMessage(msg);
            else if (role.equals(LoupGarou.class))
                for (PlayerLG lg : this.getLGs(true))
                    lg.sendMessage(msg);
            } else playerLG.sendMessage(msg);
        }
    }

    public ArrayList<PlayerLG> getAlive() {
        ArrayList<PlayerLG> alive = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (!playerLG.isDead())
                alive.add(playerLG);

        return alive;
    }

    public List<PlayerLG> getAliveExcept(PlayerLG... playersLG) {
        ArrayList<PlayerLG> alive = this.getAlive();
        alive.removeAll(Arrays.asList(playersLG));
        return alive;
    }

    public List<String> getAliveAlphabecticlySorted() {
        List<String> list = new ArrayList<>();
        this.getAlive().forEach(playerLG -> list.add(playerLG.getName()));

        list.sort(String.CASE_INSENSITIVE_ORDER);

        return list;
    }

    public List<PlayerLG> getLGs(boolean alive) {
        ArrayList<PlayerLG> lgs = new ArrayList<>();
        ArrayList<PlayerLG> addable = (ArrayList<PlayerLG>) this.playersInGame.clone();
        if (alive) addable = this.getAlive();

        for (PlayerLG playerLG : addable)
            if (playerLG.getCamp().equals(Camps.LOUP_GAROU)
                || playerLG.getRole().getClass().equals(LoupGarouBlanc.class))
                lgs.add(playerLG);

        return lgs;
    }

    public void OP(PlayerLG playerLG) {
        if (!opList.contains(playerLG.getPlayer())) this.opList.add(playerLG.getPlayer());
        playerLG.getPlayer().getInventory().setItem(6, new OpComparatorItemStack());

    }

    public void unOP(PlayerLG playerLG) {
        this.opList.remove(playerLG.getPlayer());
        playerLG.getPlayer().getInventory().remove(new OpComparatorItemStack());
    }

    public void setSpectator(PlayerLG playerLG) {
        Player player = playerLG.getPlayer();

        this.spectators.add(playerLG);
        this.playersInGame.remove(playerLG);

        this.getItemsManager().updateSpawnItems(playerLG);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(this.getPrefix() + "§9Votre mode de jeu a été établi en §7spectateur§9.");
        player.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/lg spec off§c.");
        this.sendLobbySideScoreboardToAllPlayers();
    }

    public void removeSpectator(PlayerLG playerLG) {
        Player player = playerLG.getPlayer();

        this.spectators.remove(playerLG);

        if (player.isOp()) LG.getInstance().getGame().OP(playerLG);

        this.getItemsManager().updateSpawnItems(playerLG);
        player.setMaxHealth(20);
        player.setHealth(20);

        for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
        LG.addSaturation(playerLG);
        LG.addNightVision(playerLG);

        player.sendMessage(this.getPrefix() + "§9Vous avez été retiré du mode Spectateur.");
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(Bukkit.getWorld("LG"), LG.RANDOM.nextInt(16) + 120, 16.5, LG.RANDOM.nextInt(16) + 371));
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
        this.sendLobbySideScoreboardToAllPlayers();

        return true;
    }

    public void leaveGame(PlayerLG playerLG) {
        if (playersInGame.contains(playerLG)) this.playersInGame.remove(playerLG);

        LG.getInstance().getItemsManager().updateSpawnItems(playerLG);
        playerLG.getPlayer().teleport(new Location(Bukkit.getWorld("LG"), LG.RANDOM.nextInt(16) + 120, 16.5, LG.RANDOM.nextInt(16) + 371));
        playerLG.sendTitle("§c§lVous avez été retiré de la partie !", "§6Pas de pot.", 20, 60, 20);
        playNegativeSound(playerLG.getPlayer());
        this.sendLobbySideScoreboardToAllPlayers();
    }


    public static void playNegativeSound(Player player) {
        player.playSound(player.getLocation(), Sound.DOOR_CLOSE, 8, 2);
    }

    public static void playPositiveSound(Player player) {
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 8f, 1.8f);
    }

    public void start() {
        LG.getInstance().getGame().setGameState(GameState.PLAYING);

        Bukkit.broadcastMessage(LG.getPrefix() + "§eLancement du jeu !");
        GameLG.sendTitleToAllPlayers("§b§lGO !", "", 20, 20, 20);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
            p.setTotalExperience(0);
            p.setGameMode(GameMode.ADVENTURE);

        }

        BukkitTask deal = this.dealRoles();

        this.gameRunnable = new GameRunnable(deal);
        this.gameRunnable.getMainLoop().runTaskTimer(LG.getInstance(), 0, 20L);
    }

    public BukkitTask dealRoles() {
       this.waitedPlayers = (ArrayList<PlayerLG>) this.playersInGame.clone();
        ArrayList<Role> toGive = new ArrayList<>();
        Random random = LG.RANDOM;
        try {
            Constructor<? extends Role> thiefConstructor = LG.getInstance().getRoles().get("Voleur");
            List<Constructor<? extends Role>> addedRoles = this.config.getAddedRoles();

            if (addedRoles.contains(thiefConstructor)) {
                Role role1 = addedRoles.get(random.nextInt(addedRoles.size())).newInstance();
                while (role1.getConfigName().equals("Voleur")) role1 = addedRoles.get(random.nextInt(addedRoles.size())).newInstance();

                Role role2 = addedRoles.get(random.nextInt(addedRoles.size())).newInstance();
                while (role2.getConfigName().equals("Voleur") || role1.getClass().equals(role2.getClass())) role2 = addedRoles.get(random.nextInt(addedRoles.size())).newInstance();

                addedRoles.remove(role1.getClass().getConstructor());
                addedRoles.remove(role2.getClass().getConstructor());

                Voleur.setRole1(role1);
                Voleur.setRole2(role2);
                this.rolesAtStart.add(role1);
                this.rolesAtStart.add(role2);
                this.aliveRoles.add(role1);
                this.aliveRoles.add(role2);

                System.out.println("rolevoleur : " + role1.getConfigName());
                System.out.println("rolevoleur : " + role2.getConfigName());
                for (Constructor<? extends Role> constructor : addedRoles)
                    toGive.add(constructor.newInstance());
            } else {
                int sistern = 0;
                int brothern = 0;

                for (Constructor<? extends Role> constructor : addedRoles) {
                    Role role = constructor.newInstance();

                    if (role instanceof Soeur) {
                        sistern++;
                        if (sistern % 2 != 0)
                            continue;
                    }
                    if (role instanceof Frere) {
                        brothern++;
                        if (brothern % 3 != 0)
                            continue;
                    }

                    toGive.add(constructor.newInstance());
                }
            }

            return Bukkit.getScheduler().runTaskTimer(LG.getInstance(), () -> {
                if (waitedPlayers.isEmpty()) {
                    return;
                }
                PlayerLG playerLG = this.waitedPlayers.remove(random.nextInt(this.waitedPlayers.size()));
                Role role;

                if (playerLG.getRole() != null) {
                    System.out.println("forced " + playerLG.getName());
                    role = playerLG.getRole();
                    toGive.remove(role);

                } else role = toGive.remove(random.nextInt(toGive.size()));

                GameLG.sendActionBarToAllPlayers(LG.getPrefix() + "§eDealing role to §b" + playerLG.getName());

                playerLG.joinRole(role);
                playerLG.setCamp(role.getBaseCamp());
                LG.getInstance().getItemsManager().updateStartItems(playerLG);
                this.aliveRoles.add(role);
                this.rolesAtStart.add(role);
                GameLG.this.registerEvents(role);

            }, 0, 13);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cImpossible de distribuer les rôles. Veuillez prévenir Neyuux_ ou réessayer.");
        }
        return null;
    }

    public void wait(final int seconds, final Runnable callback, final StringTimerMessage actionBarMessage, boolean cancelOthers) {
        final int[] waitTicks = {seconds * 20};
        BukkitRunnable waitRunnable = (new BukkitRunnable() {
            public void run() {
                int secondsLeft = Math.floorDiv(waitTicks[0], 20) + 1;

                for (PlayerLG playerLG : playersInGame) {
                    Player player = playerLG.getPlayer();
                    String barString = actionBarMessage.generate(playerLG, secondsLeft);

                    player.setLevel(secondsLeft);
                    player.setExp(waitTicks[0] / (seconds * 20.0F));
                    if (barString != null) playerLG.sendActionBar(barString);
                }

                GameLG.this.waitTicks = secondsLeft * 20;

                if (waitTicks[0] == 0) {
                    GameLG.this.waitTask = null;
                    cancel();
                    callback.run();
                }
                waitTicks[0]--;
            }
        });

        if (cancelOthers) cancelWait();

        this.waitTask = waitRunnable.runTaskTimer(LG.getInstance(), 0L, 1L);
    }

    public void cancelWait() {
        if (this.waitTask != null) {
            this.waitTask.cancel();
            this.waitTask = null;
        }
    }

    public void checkWin() {
        PluginManager pm = Bukkit.getPluginManager();

        if (this.getGameState().equals(GameState.FINISHED)) return;

        if (this.getAlive().size() == 1) {
            pm.callEvent(new WinEvent(WinCamps.CUSTOM));
        } else if (this.getAlive().isEmpty()) {
            pm.callEvent(new WinEvent(WinCamps.NONE));
        } else {
            int lgalive = 0;
            int villagealive = 0;
            int couplealive = 0;

            for (PlayerLG playerLG : this.getAlive()) {
                if (playerLG.getCamp().equals(Camps.LOUP_GAROU))
                    lgalive++;
                else if (playerLG.getCamp().equals(Camps.VILLAGE))
                    villagealive++;

                if (playerLG.getCache().has("couple") || (playerLG.getRole() instanceof Cupidon && (boolean)this.config.getCupiWinWithCouple().getValue()))
                    couplealive++;
            }

            if (JoueurDeFlute.getNonEnchantedPlayers().isEmpty())
                win(WinCamps.CUSTOM, Collections.singletonList(this.getPlayersByRole(JoueurDeFlute.class).get(0)));
            else if (couplealive == this.getAlive().size())
                pm.callEvent(new WinEvent(WinCamps.COUPLE));
            else if (lgalive == this.getAlive().size())
                pm.callEvent(new WinEvent(WinCamps.LOUP_GAROU));
            else if (villagealive == this.getAlive().size())
                pm.callEvent(new WinEvent(WinCamps.VILLAGE));
        }
    }

    public void win(WinCamps camp, List<PlayerLG> winners) {
        String determingName = camp.getDetermingName();
        if (camp.equals(WinCamps.CUSTOM))
            determingName = winners.get(0).getRole().getDeterminingName();

        sendTitleToAllPlayers("§c§l§n§kaa§r §e§l§nVictoire§e§l " + determingName + " §c§l§n§kaa", camp.getVictoryTitle().getSecondLine(winners), 10, 90, 20);

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(LG.getPrefix() + "§eVictoire " + determingName +"§e !");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 9f, 1f);
            PlayerLG.createPlayerLG(player).setWake();
        }

        this.setGameState(GameState.FINISHED);
        this.cancelWait();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§6Liste des §a§ljoueurs §e/ §6§lrôles §e: ");

        try {
            for (PlayerLG playerlg : playersInGame) {
                String s;
                if (winners.contains(playerlg))
                    s = "§a";
                else s = "§c";

                s = s + playerlg.getName();

                CacheLG cache = playerlg.getCache();
                HashMap<String, Constructor<? extends Role>> roles = LG.getInstance().getRoles();

                s = s + " §7» " + playerlg.getRole().getDisplayName();

                if (playerlg.getRole() instanceof Soeur) s = s + "§d (avec §o" + ((Soeur)playerlg.getRole()).getSister().getName() + "§d)";

                if (playerlg.getRole() instanceof Frere) {
                    Iterator<PlayerLG> brothers = ((Frere)playerlg.getRole()).getBrothers().iterator();
                    s = s + "§d (avec §3§o" + brothers.next().getName() + "§d et §3§o" + brothers.next().getName() + "§d)";
                }

                if (cache.has("servanteDevouee"))
                    s = s + " §d(" + roles.get("ServanteDevouee").newInstance().getDisplayName() + "§d)";

                if (cache.has("angeAtStart"))
                    s = s + " §b(" + roles.get("Ange").newInstance().getDisplayName() + "§b)";

                if (cache.has("infected")) s = s + " §c§oInfecté";

                if (cache.has("couple")) s = s + " §den couple avec §l" + ((PlayerLG)cache.get("couple")).getName();

                if (cache.has("enchanted")) s = s + " §5§oCharmé";

                if (cache.has("voleur")) s = s + " §3(" + roles.get("Voleur").newInstance().getDisplayName() + "§3)";

                if (playerlg.isMayor()) s = s + " §b§oMaire";

                Bukkit.broadcastMessage(s);
            }
            Bukkit.broadcastMessage("");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cImpossible d'afficher la suite de la liste des roles de la partie.");
            e.printStackTrace();
        }

        for (Role role : rolesAtStart)
            if (role instanceof Voleur) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§3§lRôles §3non distribués :");
                Bukkit.broadcastMessage(Voleur.getRole1().getDisplayName());
                Bukkit.broadcastMessage(Voleur.getRole2().getDisplayName());
            }

        new LGStop().runTaskTimer(LG.getInstance(), 0, 20);
    }

    public void registerEvents(Listener listener) {
        LG main = LG.getInstance();

        for (Listener listeningEventsRole : main.getListeningEventsRoles())
            if (listeningEventsRole.getClass().equals(listener.getClass()))
                return;

        Bukkit.getPluginManager().registerEvents(listener, main);
        main.getListeningEventsRoles().add(listener);
    }

    public void sendLobbySideScoreboardToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerLG.createPlayerLG(player).sendLobbySideScoreboard());
    }

    public void sendListRolesSideScoreboardToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerLG.createPlayerLG(player).sendListRolesSideScoreboard());
    }

    public void sendComedianPowersSideScoreboardToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerLG.createPlayerLG(player).sendComedianPowersSideScoreboard());
    }

    public void updatePlayersScoreboard() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerLG.createPlayerLG(player).updateGamePlayerScoreboard());
    }

    public ArrayList<PlayerLG> getPlayersByRole(Class<? extends Role> classRole) {
        ArrayList<PlayerLG> players = new ArrayList<>();

        for (PlayerLG playerLG : this.getAlive())
            if (playerLG.getRole() != null && playerLG.getRole().getClass().equals(classRole)) {
                players.add(playerLG);
            }

        return players;
    }

    public void kill(PlayerLG playerLG) {
        if (playerLG.getRole() instanceof ChaperonRouge)
            for (PlayerLG chasseurLG : this.getPlayersByRole(Chasseur.class)) {
                chasseurLG.sendMessage(LG.getPrefix() + "§aVous avez protégé le §c§lChaperon §e" + playerLG.getNameWithAttributes(chasseurLG) + "§a.");
                return;
            }

        this.getKilledPlayers().add(playerLG);
    }

    public VoteLG getMayorVote() {
        return new VoteLG("Vote du Maire", 120, true, (playerLG, secondsLeft) -> {
            if (playerLG.getCache().has("vote"))
                if (playerLG.getCache().get("vote") == null)
                    return LG.getPrefix() + "§bVous ne votez pour §3§lpersonne§e.";
                else
                    return LG.getPrefix() + "§bVous votez pour §3§l" + ((PlayerLG)playerLG.getCache().get("vote")).getName() + "§b.";

            return LG.getPrefix() + "§bIl reste §3" + secondsLeft + " secondes §bpour voter.";
        }, ChatColor.AQUA, ChatColor.DARK_AQUA, this.getAlive(), this.getAlive(), this.getAlive());
    }

    public void chooseNewMayor(DayCycle dayCycle) {
        switch ((MayorSuccession) this.getConfig().getMayorSuccession().getValue()) {
            case CHOOSE:
                PlayerLG mayorLG = this.getMayor();

                Bukkit.broadcastMessage(LG.getPrefix() + "§bUn nouveau maire doit être choisi. D'après les lois du village c'est à §3" + mayorLG.getName() + " §bde choisir son successeur.");

                this.wait(30, () -> {
                    if (dayCycle.equals(DayCycle.DAY)) this.gameRunnable.endDay(null);
                    else this.gameRunnable.endNight();
                    mayorLG.getCache().remove("unclosableInv");
                }, (param1LGPlayer, param1Int) -> LG.getPrefix() + "§bLe Maire §3§l" + mayorLG.getName() + "§b choisit son successeur...", true);

                mayorLG.getPlayer().setGameMode(GameMode.ADVENTURE);
                mayorLG.getPlayer().teleport(mayorLG.getPlacement());
                mayorLG.showAllPlayers();

                if (this.getGameType().equals(GameType.MEETING)) {
                    mayorLG.setChoosing(choosen -> {
                        if (choosen != null && !choosen.isDead()) {
                            choosen.setMayor();

                            mayorLG.getPlayer().setGameMode(GameMode.SPECTATOR);
                            if (dayCycle.equals(DayCycle.DAY)) this.gameRunnable.endDay(null);
                            else this.gameRunnable.endNight();
                        }
                    });

                } else if (this.getGameType().equals(GameType.FREE)) {
                    ChoosePlayerInv.getInventory("§bChoix du §3§lSuccesseur", mayorLG, this.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                        @Override
                        public String[] generateLore(PlayerLG paramPlayerLG) {
                            return new String[]{"§bVoulez-vous §3choisir " + paramPlayerLG.getNameWithAttributes(mayorLG) + "§bcomme successeur ?", "§bIl obtiendra le pouvoir de départager les votes.", "", "§7>>Clique pour choisir"};
                        }

                        @Override
                        public void doActionsAfterClick(PlayerLG choosenLG) {
                            choosenLG.setMayor();

                            mayorLG.getPlayer().setGameMode(GameMode.SPECTATOR);
                            mayorLG.getCache().put("unclosableInv", false);
                            mayorLG.getPlayer().closeInventory();
                            mayorLG.setSleep();
                            if (dayCycle.equals(DayCycle.DAY)) GameLG.this.gameRunnable.endDay(null);
                            else GameLG.this.gameRunnable.endNight();
                        }
                    }).open(mayorLG.getPlayer());
                    mayorLG.getCache().put("unclosableInv", true);
                }
                break;

            case REVOTE:
                Bukkit.broadcastMessage(LG.getPrefix() + "§bUn nouveau maire doit être choisi. D'après les lois du village, il doit y avoir un nouveau vote.");
                Bukkit.broadcastMessage(LG.getPrefix() + "§bVotez pour le nouveau maire !");

                VoteLG mayorVote = this.getMayorVote();
                mayorVote.start(() -> {
                    mayorVote.getChoosen().setMayor();
                    if (dayCycle.equals(DayCycle.DAY)) this.gameRunnable.endDay(null);
                    else this.gameRunnable.endNight();
                });
                break;

            default:
                if (dayCycle.equals(DayCycle.DAY)) this.gameRunnable.endDay(null);
                else this.gameRunnable.endNight();
                break;
        }
    }

    public ArrayList<PlayerLG> getKilledPlayers() {
        return killedPlayers;
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
        return this.night;
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
        if (gameType != GameType.NONE) sendTitleToAllPlayers(gameType.getName(), "§fa été choisi comme nouveau type de jeu !" , 20, 60, 20);
        this.sendLobbySideScoreboardToAllPlayers();
    }

    public ItemsManager getItemsManager() {
        return LG.getInstance().getItemsManager();
    }

    public GameRunnable getGameRunnable() {
        return this.gameRunnable;
    }

    public VoteLG getVote() {
        return this.vote;
    }

    public void setVote(VoteLG vote) {
        this.vote = vote;
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

    public int getWaitTicksToSeconds() {
        return Math.floorDiv(this.waitTicks, 20) + 1;
    }

    public boolean isNotThiefRole(Role role) {
        return Voleur.getRole1() != role && Voleur.getRole2() != role;
    }

    public interface StringTimerMessage {
        String generate(PlayerLG param1LGPlayer, int param1Int);
    }

    public interface StringVictoryTitle {
        String getSecondLine(List<PlayerLG> winners);
    }
}