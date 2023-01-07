package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class VoyanteApprentie extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lVoyante §a§lApprentie";
    }

    @Override
    public String getConfigName() {
        return "Voyante Apprentie";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lorsque qu'un joueur qui est voyante mourra, vous prendrez sa place et son rôle.";
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
    public void onVoyanteElimination(PlayerEliminationEvent ev) {
        PlayerLG playerLG = ev.getEliminated();

        if (playerLG.getRole() instanceof Voyante)
            for (PlayerLG newVoyanteLG : LG.getInstance().getGame().getPlayersByRole(this.getClass())) {
                newVoyanteLG.joinRole(playerLG.getRole());

                newVoyanteLG.sendMessage(LG.getPrefix() + "§aLa " + playerLG.getRole().getDisplayName() + " §aest morte ! Vous prenez donc vos responsabilités et prenez sa relève. Vous êtes désormais Voyante également.");
                Bukkit.broadcastMessage(LG.getPrefix() + "§dLa " + playerLG.getRole().getDisplayName() + " §deest morte ! Une " + this.getDisplayName() + " §dprend donc sa relève.");
            }
    }
}
