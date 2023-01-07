package fr.neyuux.lg.tasks;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class LGStop extends BukkitRunnable {

    private int timer = 30;
    private final GameLG game;

    public LGStop() {
        this.game = LG.getInstance().getGame();
        this.game.cancelWait();
    }

    @Override
    public void run() {

        if (!game.getGameState().equals(GameState.FINISHED)) {
            this.timer = 30;
            cancel();
            return;
        }


        if (timer == 30) {
            Bukkit.broadcastMessage(game.getPrefix() + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
        }

        if (timer == 15) {
            Bukkit.broadcastMessage(game.getPrefix() + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
        }

        if (timer == 10) {
            Bukkit.broadcastMessage(game.getPrefix() + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
        }

        if (timer <= 5 && timer > 1) {
            Bukkit.broadcastMessage(game.getPrefix() + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
        }

        if (timer == 1) {
            Bukkit.broadcastMessage(game.getPrefix() + "§eLa map va se reset dans §c§l" + timer + " §r§cseconde §e!");
        }

        if (timer == 0) {
            game.resetGame();
            cancel();
        }

        for (Player player : Bukkit.getOnlinePlayers()) player.setLevel(timer);
        timer--;
    }

}
