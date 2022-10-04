package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    public GameRunnable(BukkitTask deal) {
        this.deal = deal;
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
