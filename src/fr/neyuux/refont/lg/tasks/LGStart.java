package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import org.bukkit.Bukkit;
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


    }
}
