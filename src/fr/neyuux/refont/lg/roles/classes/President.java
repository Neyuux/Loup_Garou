package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class President extends Role {

    @Override
    public String getDisplayName() {
        return "§e§lPrésident";
    }

    @Override
    public String getConfigName() {
        return "Président";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Tous le monde connait votre identité, mais si vous mourrez, le §9village a perdu§f. Vous possédez également le rôle de maire s'il est activé.";
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

    


    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        Bukkit.broadcastMessage(LG.getPrefix() + "§aLe " + this.getDisplayName() + " §ade la partie est §e§l" + playerLG.getName());
        LG.setPlayerInScoreboardTeam("RPresident", playerLG.getPlayer());
    }
}
