package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Ancien extends Role {

    public Ancien(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§7§lAncien";
    }

    @Override
    public String getConfigName() {
        return "Ancien";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, pendant une nuit, les Loups décident de vous attaquer, vous §9survivez§f §o(utilisable qu'une seule fois)§f. Cependant si le village vous éliminent pendant le jour, tous les villageois §4perdront leurs pouvoirs§f.";
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
