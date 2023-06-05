package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class Ankou extends Role {

    @Override
    public String getDisplayName() {
        return "�6�lAnkou";
    }

    @Override
    public String getConfigName() {
        return "Ankou";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois que vous mourrez, vous pouvez �9continuer de voter�f pendant deux tours maximum depuis le cimeti�re � l'aide de la commande �e/ankou�f. Votre vote ne sera pas visible des joueurs mais sera comptabilis� et participera � l'�limination d'un joueur de jour tout en voyant les morts parler.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.ONLINE;
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
        playerLG.getCache().put("ankouVotes", 2);
        super.onPlayerJoin(playerLG);
    }

    @EventHandler
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();

        for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersByRole(this.getClass())) {
            if ((int)playerLG.getCache().get("ankouVotes") > 0 && playerLG.isDead())
                vote.getVoters().add(playerLG);
        }

    }
}
