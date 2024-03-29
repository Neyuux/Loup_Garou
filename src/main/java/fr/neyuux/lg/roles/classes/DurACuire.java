package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.DayCycle;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

public class DurACuire extends Role {

    @Override
    public String getDisplayName() {
        return "�c�lDur �c� �6�lCuire";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lorque les Loups voudront vous tuer, vous survivrez jusqu'au jour suivant.";
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

    @EventHandler
    public void onElimination(PlayerEliminationEvent ev) {
        PlayerLG playerLG = ev.getEliminated();

        if (LG.getInstance().getGame().getDayCycle().equals(DayCycle.NIGHT)) {
            if (playerLG.getRole() instanceof DurACuire) {
                if (!playerLG.getCache().has("durACuire")) playerLG.getCache().put("durACuire", 2);
                if ((int)playerLG.getCache().get("durACuire") != 0) ev.setCancelled(true);
            }
        }
    }
}
