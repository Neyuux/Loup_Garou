package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Pretre extends Role {

    public Pretre(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�e�lPr�tre";
    }

    @Override
    public String getScoreboardName() {
        return "�e�lPr�tre";
    }

    @Override
    public String getConfigName() {
        return "Pretre";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous poss�dez une fiole d'eau b�nite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il �9mourra�f sinon, vous mourrez.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �e" + this.getTimeout() + " secondes �fpour lancer de l'eau b�nite sur quelqu'un.";
    }
}
