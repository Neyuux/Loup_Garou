package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.event.PlayerEliminationEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class ChevalierALEpeeRouillee extends Role {

    @Override
    public String getDisplayName() {
        return "§7§lChevalier à l'Épée §6§lRouillée";
    }

    @Override
    public String getConfigName() {
        return "Chevalier a l'Épée Rouillée";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Si vous mourrez par les loups, le premier Loup dont le pseudo vous suit dans l'ordre alphabétique§f tombera gravement malade : §9il mourra§f au tour suivant.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
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
    public void onEliminationLG(PlayerEliminationEvent ev) {
        PlayerLG playerLG = ev.getEliminated();

        if (playerLG.getRole().equals(this) && LoupGarou.getLastTargetedByLG().equals(playerLG)) {
            List<String> list = LG.getInstance().getGame().getAliveAlphabecticlySorted();
            while (!list.get(0).equals(playerLG.getName())) {
                String s = list.remove(0);
                list.add(s);
            }

            for (String s : list) {
                PlayerLG sickLG = PlayerLG.createPlayerLG(Bukkit.getPlayer(s));

                if (!s.equals(playerLG.getName()) && sickLG.isLG())
                    sickLG.getCache().put("chevalierALEppeRouilleSick", playerLG);
            }
        }
    }

    @EventHandler
    public void onNightEndEvent(NightEndEvent ev) {
        GameLG game = LG.getInstance().getGame();
        for (PlayerLG playerLG : game.getAlive())
            if (playerLG.getCache().has("chevalierALEppeRouilleSick"))
                game.kill(playerLG);
    }
    
}
