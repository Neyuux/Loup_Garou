package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

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
}
