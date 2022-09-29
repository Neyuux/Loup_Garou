package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Pyromane extends Role {

    @Override
    public String getDisplayName() {
        return "�6�lPyromane";
    }

    @Override
    public String getConfigName() {
        return "Pyromane";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les joueurs de la partie et de, par cons�quent, de gagner seul. Chaque nuit, vous pourrez d�cider d'enrober un joueur d'essence ou de mettre le feu � tous les joueurs d�j� huil�s...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �6" + "timer restant" + " secondes�f pour r�pandre de l'essence sur deux personnes."; //TODO
    }
}
