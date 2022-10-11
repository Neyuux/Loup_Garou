package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleNightOrder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    private final GameLG game;

    private int night = 1;

    private List<Role> rolesOrder = new ArrayList<>();

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

        if (deal != null && this.checkDealFinished()) {
            for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersInGame())
                playerLG.updateGamePlayerScoreboard();

            this.game.wait(6, this::nextNight, (playerLG, secondsLeft) -> LG.getPrefix() + "§9Début de la nuit dans §1§l" + secondsLeft + "§9 seconde" + LG.getPlurial(secondsLeft)  + ".");
        }
    }


    private boolean checkDealFinished() {
        for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersInGame())
            if (playerLG.getRole() == null) return false;

        this.deal.cancel();
        this.deal = null;
        return true;
    }


    public void nextNight() {

        if ((boolean)this.game.getConfig().getDayCycle().getValue())
            Bukkit.getWorld("LG").setTime(18000);

        Bukkit.broadcastMessage("      §9§lNUIT " + this.night);
        for (Player player : Bukkit.getOnlinePlayers())
            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 4, 0.1f);

        for (RoleNightOrder order : RoleNightOrder.values())
            for (Role role : this.game.getAliveRoles())
                if (role.getClass().getName().equals(order.getRoleClass().getName()))
                    this.rolesOrder.add(role);

        for (PlayerLG playerLG : this.game.getPlayersInGame())
            playerLG.setSleep();

        this.night++;
    }
}
