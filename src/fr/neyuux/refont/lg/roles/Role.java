package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class Role implements Listener {

    private final ArrayList<PlayerLG> players = new ArrayList<>();


    public Role() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }

    public void onDistribution(PlayerLG player) {
        this.players.add(player);
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
            return -1;
        }
    }

    public ArrayList<PlayerLG> getPlayers() {
        return players;
    }

    public void onPlayerJoin(PlayerLG playerLG) {
        this.players.add(playerLG);

        System.out.println(playerLG.getName() + " >> " + this.getConfigName());

        playerLG.sendTitle("§fVous êtes " + this.getDisplayName(), "§fVotre camp : §e" + this.getBaseCamp().getName(), 10, 60, 10);
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

enum RoleNightOrder {
    VOLEUR,
    CUPIDON,
    ENFANTSAUVAGE,
    CHIENLOUP,
    JUMEAU,
    NOCTAMBULE,
    COMEDIEN,
    VOYANTE,
    VOYANTEDAURA,
    ENCHANTEUR,
    DETECTIVE,
    RENARD,
    PACIFISTE,
    FILLEDEJOIE,
    GARDEDUCORPS,
    SALVATEUR,
    LOUPGAROU,
    INFECTPEREDESLOUPS,
    GRANDMECHANTLOUP,
    LOUPGAROUBLANC,
    PETITEFILLEWO,
    SORCIERE,
    PRETRE,
    NECROMANCIEN,
    VILAINGARCON,
    DICTATEUR,
    MAMIEGRINCHEUSE,
    CORBEAU,
    JOUEURDEFLUTE,
    PYROMANE,
    SOEUR,
    FRERE
}
