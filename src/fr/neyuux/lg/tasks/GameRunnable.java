package fr.neyuux.lg.tasks;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.DayCycle;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.DayEndEvent;
import fr.neyuux.lg.event.DayStartEvent;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.RoleNightOrder;
import fr.neyuux.lg.roles.classes.Comedien;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    private final GameLG game;

    private final List<Role> rolesOrder = new ArrayList<>();

    private final HashSet<List<Double>> usedLocations;
    //TODO BUG

    private int timer = 7;

    public GameRunnable(BukkitTask deal) {
        this.deal = deal;
        this.game = LG.getInstance().getGame();
        this.usedLocations = new HashSet<>();
    }


    @Override
    public void run() {
        if (!LG.getInstance().getGame().getGameState().equals(GameState.PLAYING)) {
            cancel();
            return;
        }

        if (this.game.getDayCycle().equals(DayCycle.NIGHT) && this.game.getGameType().equals(GameType.FREE))
            this.checkSleep();

        if (deal != null && this.checkDealFinished()) {
            for (PlayerLG playerLG : LG.getInstance().getGame().getAlive()) {
                playerLG.updateGamePlayerScoreboard();

                FileConfiguration file = LG.getInstance().getConfig();
                List<List<Double>> placementlists = (List<List<Double>>) file.getList("spawns." + LG.getInstance().getGame().getGameType());
                List<Double> doubleList = placementlists.get(new Random().nextInt(placementlists.size()));
                if (this.usedLocations.contains(doubleList))
                    while (this.usedLocations.contains(doubleList)) {
                        doubleList = placementlists.get(new Random().nextInt(placementlists.size()));
                    }

                Location placement = new Location(Bukkit.getWorld("LG"), doubleList.get(0) + 0.5, doubleList.get(1), doubleList.get(2) + 0.5, doubleList.get(3).floatValue(), doubleList.get(4).floatValue());

                this.usedLocations.add(doubleList);
                System.out.println(this.usedLocations);
                playerLG.setPlacement(placement);
            }

            this.game.wait(6, this::nextNight, (playerLG, secondsLeft) -> LG.getPrefix() + "§9Début de la nuit dans §1§l" + secondsLeft + "§9 seconde" + LG.getPlurial(secondsLeft)  + ".", false);
        }

        if (timer == 0 && game.getDayCycle() != DayCycle.NONE) {
            timer = 14;
            if (game.getPlayersByRole(Comedien.class).isEmpty())
                game.sendListRolesSideScoreboardToAllPlayers();
            else
                game.sendComedianPowersSideScoreboardToAllPlayers();
        }

        if (timer == 7 && game.getDayCycle() != DayCycle.NONE) {
            game.sendListRolesSideScoreboardToAllPlayers();
        }

        timer--;
    }


    private boolean checkDealFinished() {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG.getRole() == null) return false;

        this.deal.cancel();
        this.deal = null;
        return true;
    }


    public void nextNight() {
        if (game.getGameState().equals(GameState.FINISHED)) return;

        Bukkit.getPluginManager().callEvent(new NightStartEvent());

        game.addNight();

        game.setDayCycle(DayCycle.NIGHT);

        if ((boolean)this.game.getConfig().getDayCycle().getValue())
            Bukkit.getWorld("LG").setTime(18000);

        Bukkit.broadcastMessage("      §9§lNUIT " + game.getNight());
        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 4, 0.1f);

        this.calculateRoleOrder();

        for (PlayerLG playerLG : this.game.getAlive())
            playerLG.setSleep();


        new Runnable() {

            public void run() {
                Runnable callback = this;

                new BukkitRunnable() {

                    public void run() {

                        if (GameRunnable.this.rolesOrder.isEmpty()) {
                            GameRunnable.this.endNight();
                            System.out.println("endnight");
                            return;
                        }

                        GameRunnable.this.rolesOrder.remove(0).newNightTurn(callback);

                    }
                }.runTaskLater(LG.getInstance(), 5L);
            }
        }.run();
    }

    public void nextDay() {
        if (game.getGameState().equals(GameState.FINISHED)) return;

        Bukkit.getPluginManager().callEvent(new DayStartEvent());

        game.addDay();

        game.setDayCycle(DayCycle.DAY);

        if ((boolean)this.game.getConfig().getDayCycle().getValue())
            Bukkit.getWorld("LG").setTime(0);

        Bukkit.broadcastMessage("      §e§lJOUR " + game.getDay());
        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.CHICKEN_IDLE, 4f, 0.5f);

        this.continueDay();

    }

    public void continueDay() {
        if ((boolean)game.getConfig().getMayor().getValue() && game.getMayor() == null && game.getDay() == 1) {
            Bukkit.broadcastMessage(LG.getPrefix() + "§bUn Maire doit être choisi pour gouverner le Village. Il aura le pouvoir de départager les votes en cas d'égalité. Choisissez-le avec précaution, un Loup-Garou ne doit pas avoir ce rôle.");
            Bukkit.broadcastMessage(LG.getPrefix() + "§bVous avez 2 minutes pour choisir le Maire du Village.");

            VoteLG mayorVote = game.getMayorVote();
            mayorVote.start(()-> {
                mayorVote.getChoosen().setMayor();
                this.continueDay();
            });
        } else {
            Bukkit.broadcastMessage(LG.getPrefix() + "§eC'est l'heure du Vote du Village ! A vous de voter avec minutie pour un joueur qui vous semble être un Loup-Garou. Le joueur qui obtiendra le plus de votes sera §céliminé§e de la partie.");
            Bukkit.broadcastMessage(LG.getPrefix() + "§eVous avez 3 minutes pour choisir qui éliminer aujourd'hui.");

            VoteLG voteLG = new VoteLG("Vote du Village", 180, true, (playerLG, secondsLeft) -> {
                if (playerLG.getCache().has("vote"))
                    if (playerLG.getCache().get("vote") == null)
                        return LG.getPrefix() + "§eVous ne votez pour §6§lpersonne§e.";
                    else
                        return LG.getPrefix() + "§eVous votez pour §6§l" + ((PlayerLG)playerLG.getCache().get("vote")).getName() + "§e.";

                return LG.getPrefix() + "§eIl reste §6" + secondsLeft + " secondes §epour voter.";
            }, ChatColor.YELLOW, ChatColor.GOLD, game.getAlive(), game.getAlive(), game.getAlive());

            voteLG.start(()-> GameRunnable.this.endDay(voteLG.getChoosen()));
        }
    }

    public void endNight() {
        NightEndEvent event = new NightEndEvent();
        Bukkit.getPluginManager().callEvent(event);

        Bukkit.broadcastMessage("");

        for (PlayerLG playerLG : game.getKilledPlayers())
            playerLG.eliminate();
        game.getKilledPlayers().clear();

        game.getAlive().forEach(PlayerLG::setWake);

        if (event.isCancelled()) return;

        if (game.getMayor() != null && game.getMayor().isDead() && game.getMayor().getPlayer().isOnline()) {
            game.chooseNewMayor(DayCycle.NIGHT);
            return;
        }

        this.nextDay();
    }

    public void endDay(PlayerLG killedLG) {
        DayEndEvent event = new DayEndEvent(killedLG);
        Bukkit.getPluginManager().callEvent(event);

        if (killedLG != null) killedLG.eliminate();

        if (event.isCancelled()) return;

        if (game.getMayor() != null && game.getMayor().isDead() && game.getMayor().getPlayer().isOnline()) {
            game.chooseNewMayor(DayCycle.DAY);
            return;
        }

        this.nextNight();
    }

    public void checkSleep() {
        for (PlayerLG playerLG : this.game.getAlive())
            if (playerLG.isSleeping() && !playerLG.getPlayer().isSleeping())
                playerLG.setSleep();
    }


    public List<Role> getRolesOrder() {
        return rolesOrder;
    }

    public void calculateRoleOrder() {
        System.out.println("rolesorder :");
        for (RoleNightOrder order : RoleNightOrder.values())
            for (Role role : this.game.getRolesAtStart())
                if (role.getClass().getName().equals(order.getRoleClass().getName()))
                    if (order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.EACH_NIGHT) || (order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.ONE_OUT_OF_TWO) && this.game.getNight() % 2 == 0) || (order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.FIRST_NIGHT) && this.game.getNight() == 1)) {
                        this.rolesOrder.add(role);
                         System.out.println("  " + role.getConfigName());
                    }
    }
}
