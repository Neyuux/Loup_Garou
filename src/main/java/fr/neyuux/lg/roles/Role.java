package fr.neyuux.lg.roles;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public abstract class Role implements Listener {

    public ArrayList<PlayerLG> players;

    public void newNightTurn(Runnable callback) {
        this.updatePlayers();
        this.onNightTurn(callback);
    }

    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();
        if (players.isEmpty()) {
            if (game.isNotThiefRole(this)) {
                callback.run();
            } else {
                LG.getInstance().getGame().wait(Role.this.getTimeout(), () -> {

                }, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Role.this.getDeterminingName(), true);
                Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                    game.cancelWait();
                    callback.run();
                }, (LG.RANDOM.nextInt(7) + 5) * 20L);
            }
            return;
        }


        PlayerLG playerLG = players.remove(0);

        if (playerLG.canUsePowers()) {

            playerLG.setWake();

            game.wait(Role.this.getTimeout(), () -> {
                this.onPlayerTurnFinish(playerLG);
                this.onNightTurn(callback);

            }, (currentPlayer, secondsLeft) -> {
                if (game.getVote() == null || !game.getVote().getVoters().contains(currentPlayer))
                    return LG.getPrefix() + "Au tour " + Role.this.getDeterminingName();

                if (game.getVote() != null)
                    return null;
                else {
                    if (currentPlayer == playerLG)
                        return "§9§lA toi de jouer !";
                    else
                        return LG.getPrefix() + "Au tour " + Role.this.getDeterminingName();
                }
            }, true);

            playerLG.sendMessage(LG.getPrefix() + Role.this.getActionMessage());
            Role.this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));

        } else {
            game.wait(Role.this.getTimeout(), () -> {}, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Role.this.getDeterminingName(), true);
            Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                game.cancelWait();
                onNightTurn(callback);
            }, (LG.RANDOM.nextInt(7) + 5) * 20L);
        }
    }

    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        playerLG.getPlayer().playSound(playerLG.getLocation(), Sound.LEVEL_UP, 6f, 1.5f);
    }

    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.stopChoosing();
        LG.closeSmartInv(playerLG.getPlayer());
        playerLG.setSleep();
    }


    public void onPlayerJoin(PlayerLG playerLG) {
        System.out.println(playerLG.getName() + " >> " + this.getConfigName());

        playerLG.sendTitle("§fVous êtes " + this.getDisplayName(), "§fVotre camp : §e" + this.getBaseCamp().getName(), 10, 60, 10);
        playerLG.sendMessage(LG.getPrefix() + this.getDescription());
        //TODO give role map
    }

    private void updatePlayers() {
        this.players = LG.getInstance().getGame().getPlayersByRole(this.getClass());
    }


    public abstract String getDisplayName();

    public abstract String getConfigName();

    public abstract String getDeterminingName();

    public abstract int getMaxNumber();

    public abstract String getDescription();

    public abstract Camps getBaseCamp();

    public abstract Decks getDeck();

    public abstract int getTimeout();

    public abstract String getActionMessage();
}

