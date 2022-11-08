package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class DurACuire extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lDur §cà §6§lCuire";
    }

    @Override
    public String getConfigName() {
        return "Dur a Cuire";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lorque les Loups voudront vous tuer, vous survivrez jusqu'au jour suivant.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }


    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG.getCache().has("durACuire"))
                playerLG.getCache().put("durACuire", (int)playerLG.getCache().get("durACuire") - 1);
    }
}
