package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Salvateur extends Role {

    @Override
    public String getDisplayName() {
        return "�e�lSalvateur";
    }

    @Override
    public String getConfigName() {
        return "Salvateur";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Chaque nuit, vous pourrez �9prot�ger�f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas prot�ger deux fois la m�me personne.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �e" + this.getTimeout() + " secondes�f pour prot�ger quelqu'un.";
    }
}
