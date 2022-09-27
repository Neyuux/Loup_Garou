package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.old.lg.Gstate;
import fr.neyuux.refont.old.lg.task.GameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LGStart extends BukkitRunnable {

    private int timer = 10;
    private final GameLG game;

    public LGStart(GameLG game) {
        this.game = game;
    }

    @Override
    public void run() {

        if (this.game.getPlayersInGame().size() + this.game.getSpectators().size() != Bukkit.getOnlinePlayers().size()) {
            this.game.setGameState(GameState.WAITING);
        }

        if (this.game.getGameType().equals(GameType.MEETING) && this.game.getPlayersInGame().size() > 12) {
            this.game.setGameState(GameState.WAITING);
            Bukkit.broadcastMessage(LG.getPrefix() + "§c§l12 JOUEURS MAXIMUM POUR LE MODE RÉUNION");
        }

        if (!this.game.getGameState().equals(GameState.STARTING)) {
            cancel();
            return;
        }


        for(Player pls : Bukkit.getOnlinePlayers()) {
            pls.setLevel(timer);
            pls.setExp((float)timer / 10);
        }


        if (timer != 0) {
            Bukkit.broadcastMessage(LG.getPrefix() + "§eLancement du jeu dans §c§l" + timer + "§c seconde" + LG.getPlurial(timer) + " !");
        }

        if (timer==10) {
            GameLG.sendTitleToAllPlayers("§4§l10", "§c§oPréparation...", 15, 30, 15);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 2f);

        } else if (timer==5) {
            GameLG.sendTitleToAllPlayers("§6§l5", "§cAttention !", 5, 10, 5);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.7f);

        } else if (timer==4) {
            GameLG.sendTitleToAllPlayers("§e§l4", "§cAttention !", 5, 10, 5);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.5f);

        } else if (timer==3) {
            GameLG.sendTitleToAllPlayers("§2§l3", "§6A vos marques...", 5, 10, 5);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.2f);

        } else if (timer==2) {
            GameLG.sendTitleToAllPlayers("§a§l2", "§ePrêts ?", 5, 10, 5);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.1f);

        } else if (timer==1) {
            GameLG.sendTitleToAllPlayers("§f§l1", "§aDécollage...", 5, 10, 5);
            for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1f);

        } else if (timer == 0) {
           LG.getInstance().getGame().start();
        }

        timer--;
    }
}
