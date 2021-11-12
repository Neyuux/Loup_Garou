package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class FilleDeJoie extends Role {

    public FilleDeJoie(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§d§lFille de Joie";
    }

    @Override
    public String getConfigName() {
        return "Fille de Joie";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez aller ken un joueur. Si ce joueur est un Loup ou est mangé par les Loups, vous §9mourrez§f. Si les Loups essaient de vous tuer pendant que vous êtes chez quelqu'un d'autre, vous §9survivez§f.";
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
        return "§fVous avez §d" + this.getTimeout() + " secondes§f pour choisir chez qui aller cette nuit.";
    }
}
