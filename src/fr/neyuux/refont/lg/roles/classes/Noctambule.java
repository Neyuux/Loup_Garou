package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Noctambule extends Role {
    @Override
    public String getDisplayName() {
        return "�9�lNoctambule";
    }

    @Override
    public String getConfigName() {
        return "Noctambule";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous devez choisir un joueur chez qui �9dormir�f, ce joueur conna�tra alors votre identit� mais sera priv� de ses pouvoirs pour la nuit.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �9" + this.getTimeout() + " secondes�f pour aller dormir chez quelqu'un.";
    }
}
