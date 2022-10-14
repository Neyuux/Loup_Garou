package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

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

        new Runnable() {

            public void run() {

                game.cancelWait();

                if (players.size() == 0) {
                    Role.this.onFinishNightTurn(callback);
                    return;
                }

                PlayerLG playerLG = players.remove(0);

                if (playerLG.canUsePowers()) {

                    game.wait(Role.this.getTimeout(), this, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : "Au tour " + Role.this.getDeterminingName());
                    playerLG.sendMessage("" + Role.this.getActionMessage());
                    Role.this.onRoleNightTurn(playerLG, this);

                } else {
                    game.wait(Role.this.getTimeout(), () -> {

                    }, (currentPlayer, secondsLeft) -> (currentPlayer == player) ? "ne peux pas jouer" : ("au tour " + Role.this.getFriendlyName() + " + secondsLeft + " s));
                    final Runnable run = this;
                    (new BukkitRunnable() {
                        public void run() {
                            run.run();
                        }
                    }).runTaskLater((Plugin)MainLg.getInstance(), (20 * (ThreadLocalRandom.current().nextInt(Role.this.getTimeout() / 3 * 2 - 4) + 4)));
                }
            }
        }.run();
    }

    public void onFinishNightTurn(Runnable callback) {

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

