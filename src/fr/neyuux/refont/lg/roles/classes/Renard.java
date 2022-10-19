package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Renard extends Role {

    private static final List<PlayerLG> players = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "§6§lRenard";
    }

    @Override
    public String getConfigName() {
        return "Renard";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez sélectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un §c§lLoup-Garou§f, vous §9gardez votre pouvoir§f. Par contre, s'il n'y en a aucun, vous §9perdez votre pouvoir§f.";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §6" + this.getTimeout() + " secondes §fpour renifler quelqu'un.";
    }
}
