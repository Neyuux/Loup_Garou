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

    private static final ArrayList<PlayerLG> players = new ArrayList<>();


    public Role() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }

    public void onNightTurn() {

    }

    public void onFinishNightTurn() {

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

    public static ArrayList<PlayerLG> getPlayers() {
        return players;
    }

    public void onPlayerJoin(PlayerLG playerLG) {
        players.add(playerLG);

        System.out.println(playerLG.getName() + " >> " + this.getConfigName());

        playerLG.sendTitle("�fVous �tes " + this.getDisplayName(), "�fVotre camp : �e" + this.getBaseCamp().getName(), 10, 60, 10);
        playerLG.sendMessage(LG.getPrefix() + this.getDescription());
        //TODO give role map
    }


    public abstract String getDisplayName();

    public abstract String getConfigName();

    public abstract int getMaxNumber();

    public abstract String getDescription();

    public abstract Camps getBaseCamp();

    public abstract Decks getDeck();

    public abstract int getTimeout();

    public abstract String getActionMessage();
}

