package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    private final GameLG game;

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

            this.game.wait(6, () -> {
                nextNight
            });
        }
    }


    private boolean checkDealFinished() {
        for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersInGame())
            if (playerLG.getRole() == null) return false;

        this.deal.cancel();
        this.deal = null;
        return true;
    }
}
