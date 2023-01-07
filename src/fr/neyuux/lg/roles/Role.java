package fr.neyuux.lg.roles;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class Role implements Listener {

    public ArrayList<PlayerLG> players;


    public Role() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }


    public void newNightTurn(Runnable callback) {
        this.updatePlayers();
        this.onNightTurn(callback);
    }

    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (players.isEmpty()) {
            if (game.isNotThiefRole(this)) callback.run();
            else LG.getInstance().getGame().wait(Role.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Role.this.getDeterminingName(), true);
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
                    return "�9�lAu tour " + Role.this.getDeterminingName();

                if (game.getVote() != null)
                    return null;
                else {
                    if (currentPlayer == playerLG)
                        return "�9�lA toi de jouer !";
                    else
                        return "�9�lAu tour " + Role.this.getDeterminingName();
                }
            }, true);

            playerLG.sendMessage(LG.getPrefix() + Role.this.getActionMessage());
            Role.this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));

        } else {
            LG.getInstance().getGame().wait(Role.this.getTimeout() / 4, () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Role.this.getDeterminingName(), true);
        }
    }

    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {

    }

    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.stopChoosing();
        playerLG.getPlayer().closeInventory();
        playerLG.setSleep();
    }


    public int getTurnNumber() {
        String className = this.getClass().getSimpleName().toUpperCase();
        try {
            RoleNightOrder roleNightOrder = RoleNightOrder.valueOf(className);
            return roleNightOrder.ordinal();
        } catch (IllegalArgumentException e) {
            LG.getInstance().getLogger().log(Level.SEVERE, className + " n'a pas le meme nom dans RoleNightOrder");
            Bukkit.broadcastMessage(LG.getPrefix() + "�4[�cErreur�4] �cImpossible de r�cup�rer l'ordre de passage de nuit. Veuillez appeler Neyuux_ ou r�essayer plus tard.");
            return -1;
        }
    }

    public void onPlayerJoin(PlayerLG playerLG) {
        System.out.println(playerLG.getName() + " >> " + this.getConfigName());

        playerLG.sendTitle("�fVous �tes " + this.getDisplayName(), "�fVotre camp : �e" + this.getBaseCamp().getName(), 10, 60, 10);
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
