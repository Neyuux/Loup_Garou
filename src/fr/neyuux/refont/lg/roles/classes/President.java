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
        return "�e�lPr�sident";
    }

    @Override
    public String getConfigName() {
        return "Pr�sident";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tous le monde connait votre identit�, mais si vous mourrez, le �9village a perdu�f. Vous poss�dez �galement le r�le de maire s'il est activ�.";
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

        Bukkit.broadcastMessage(LG.getPrefix() + "�aLe " + this.getDisplayName() + " �ade la partie est �e�l" + playerLG.getName());
        LG.setPlayerInScoreboardTeam("RPresident", playerLG.getPlayer());
    }
}
