package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Jumeau extends Role {

    @Override
    public String getDisplayName() {
        return "�5�lJumeau";
    }

    @Override
    public String getConfigName() {
        return "Jumeau";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lors de la premi�re nuit, vous devrez choisir un joueur. Lorsque ce joueur mourra, vous obtiendrez son r�le.";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �5" + this.getTimeout() + " secondes �fpour choisir qui va �tre votre jumeau.";
    }
}
