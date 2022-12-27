package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.DayEndEvent;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.event.NightStartEvent;
import fr.neyuux.refont.lg.event.VoteEndEvent;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleNightOrder;
import fr.neyuux.refont.lg.roles.classes.Bouffon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    private final GameLG game;

    private final List<Role> rolesOrder = new ArrayList<>();

    public GameRunnable(BukkitTask deal) {
        this.deal = deal;
        this.game = LG.getInstance().getGame();
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
                Location placement = new Location(Bukkit.getWorld("LG"), doubleList.get(0) + 0.5, doubleList.get(1), doubleList.get(2) + 0.5, doubleList.get(3).floatValue(), doubleList.get(4).floatValue());

                playerLG.setPlacement(placement);
            }

            this.game.wait(6, this::nextNight, (playerLG, secondsLeft) -> LG.getPrefix() + "§9Début de la nuit dans §1§l" + secondsLeft + "§9 seconde" + LG.getPlurial(secondsLeft)  + ".", false);
        }
    }


    private boolean checkDealFinished() {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG.getRole() == null) return false;

        this.deal.cancel();
        this.deal = null;
        return true;
    }


    public void nextNight() {

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
    }

    public void endNight() {
        NightEndEvent event = new NightEndEvent();
        Bukkit.getPluginManager().callEvent(event);

        for (PlayerLG playerLG : game.getKilledPlayers())
            playerLG.eliminate();
        game.getKilledPlayers().clear();

        if (event.isCancelled()) return;

        this.nextDay();
    }

    public void endDay(PlayerLG killedLG) {
        DayEndEvent event = new DayEndEvent();
        Bukkit.getPluginManager().callEvent(event);

        if (killedLG != null) killedLG.eliminate();

        if (event.isCancelled()) return;

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
                    if (order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.EACH_NIGHT) || order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.ONE_OUT_OF_TWO) && this.game.getNight() % 2 == 0 || order.getRecurrenceType().equals(RoleNightOrder.RecurrenceType.FIRST_NIGHT) && this.game.getNight() == 1) {
                        this.rolesOrder.add(role);
                         System.out.println("  " + role.getConfigName());
                    }
    }
}
