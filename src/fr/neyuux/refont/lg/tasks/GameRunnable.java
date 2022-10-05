package fr.neyuux.refont.lg.tasks;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameRunnable extends BukkitRunnable {

    private BukkitTask deal;

    private final GameLG game;

    private int night = 1;

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

            this.game.wait(6, this::nextNight, LG.getPrefix() + "�9D�but de la nuit dans �1�l" + this.game.getWaitTicksToSeconds() + "�9 seconde" + LG.getPlurial(this.game.getWaitTicksToSeconds())  + ".");
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


        this.night++;
    }
}
