package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

import java.util.List;

public class Pirate extends Role {

    @Override
    public String getDisplayName() {
        return "§e§lPirate";
    }

    @Override
    public String getConfigName() {
        return "Pirate";
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
        return "§fVous êtes "+this.getDisplayName()+", votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez prendre un joueur en otage. Cela vous permettra §9faire tuer§f votre otage à votre place si vous êtes visé par le vote du village.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §e" + this.getTimeout() + " secondes §fpour voter pour prendre en otage.";
    }

    
}
