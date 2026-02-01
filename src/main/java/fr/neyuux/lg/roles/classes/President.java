package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.WinCamps;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

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
        return "du " + this.getDisplayName();
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

        if (LG.getInstance().getGame().getConfig().getMayor().getValue().equals(true)) {
            playerLG.setMayor();
        }
    }

    @EventHandler
    public void onElimination(PlayerEliminationEvent ev) {
        if (ev.getEliminated().getRole() instanceof President) {
            GameLG game = LG.getInstance().getGame();
            game.win(WinCamps.LOUP_GAROU, game.getAlive());
        }
    }

}
