package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Comedien extends Role {

    public Comedien(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§5§lComédien";
    }

    @Override
    public String getScoreboardName() {
        return "§5§lComédien";
    }

    @Override
    public String getConfigName() {
        return "Comédien";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous obtiendrez §93 pouvoirs de rôles villageois§f que vous pourrez utiliser une fois chacun pendant la partie.";
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
        return "§fVous avez §d" + getTimeout() + " secondes §fpour choisir un rôle pour cette nuit.";
    }
}
