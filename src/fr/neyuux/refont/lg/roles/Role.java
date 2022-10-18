package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class Role implements Listener {

    private final ArrayList<PlayerLG> players = new ArrayList<>();


    public Role() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }


    public void onNightTurn(Runnable callback) {
        ArrayList<PlayerLG> players = (ArrayList<PlayerLG>) this.getPlayers().clone();
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (this.players.isEmpty()) {
            if (!game.isThiefRole(this)) {
                callback.run();
                return;
            } else {
                createFakeTurn(callback);
            }
        }

        PlayerLG playerLG = players.remove(0);

        if (playerLG.canUsePowers()) {

            playerLG.setWake();

            game.wait(Role.this.getTimeout(), () -> {
                playerLG.setSleep();
                this.onNightTurn(callback);

            }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : "Au tour " + Role.this.getDeterminingName());

            playerLG.sendMessage("" + Role.this.getActionMessage());
            Role.this.onRoleNightTurn(playerLG, () -> this.onNightTurn(callback));

        } else {
            createFakeTurn(callback);
        }
    }

    public void createFakeTurn(Runnable callback) {
        LG.getInstance().getGame().wait(Role.this.getTimeout() / 4, () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> "Au tour " + Role.this.getDeterminingName());
    }

    protected void onRoleNightTurn(PlayerLG playerLG, Runnable callback) {

    }


    public int getTurnNumber() {
        String className = this.getClass().getSimpleName().toUpperCase();
        try {
            RoleNightOrder roleNightOrder = RoleNightOrder.valueOf(className);
            return roleNightOrder.ordinal();
        } catch (IllegalArgumentException e) {
            LG.getInstance().getLogger().log(Level.SEVERE, className + " n'a pas le meme nom dans RoleNightOrder");
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cImpossible de récupérer l'ordre de passage de nuit. Veuillez appeler Neyuux_ ou réessayer plus tard.");
            return -1;
        }
    }

    public ArrayList<PlayerLG> getPlayers() {
        return players;
    }

    public void onPlayerJoin(PlayerLG playerLG) {
        players.add(playerLG);

        System.out.println(playerLG.getName() + " >> " + this.getConfigName());

        playerLG.sendTitle("§fVous êtes " + this.getDisplayName(), "§fVotre camp : §e" + this.getBaseCamp().getName(), 10, 60, 10);
        playerLG.sendMessage(LG.getPrefix() + this.getDescription());
        //TODO give role map
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

