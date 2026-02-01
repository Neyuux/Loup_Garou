package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.enums.DayCycle;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

import java.util.HashSet;

public class PetiteFille extends Role {

    @Override
    public String getDisplayName() {
        return "§9§lPetite §b§lFille";
    }

    @Override
    public String getConfigName() {
        return "Petite Fille";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Chaque nuit, à la levée des Loups-Garous : vous pourrez §9espionner leurs messages§f.";
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
    public void onVoteStart(VoteStartEvent ev) {
        GameLG game = LG.getInstance().getGame();

        if (game.getDayCycle().equals(DayCycle.NIGHT))
            LoupGarou.CHAT.addSpies(new HashSet<>(game.getPlayersByRole(this.getClass())));
    }
}
