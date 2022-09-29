package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class ChevalierALEpeeRouillee extends Role {

    @Override
    public String getDisplayName() {
        return "§7§lChevalier §eà l'§7Épée §6§lRouillée";
    }

    @Override
    public String getConfigName() {
        return "Chevalier a l'Épée Rouillée";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Si vous mourrez par les loups, le premier Loup à votre gauche§o(réunion)§f ou en dessous dans le tab§o(libre)§f tombera gravement malade : §9il mourra§f au tour suivant.";
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
}
