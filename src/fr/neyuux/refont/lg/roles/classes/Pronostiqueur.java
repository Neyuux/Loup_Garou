package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class Pronostiqueur extends Role {

    @Override
    public String getDisplayName() {
        return "�7�lPronostiqueur";
    }

    @Override
    public String getConfigName() {
        return "Pronostiqueur";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez calculer la probabilit� qu'un joueur soit m�chant. Un message affichera si le joueur est m�chant ou non. Cependant, vos pronostics sont soit �atous bons�f, soit �ctous mauvais�f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �d " + this.getTimeout() + " secondes�f pour examiner quelqu'un.";
    }

    
}
