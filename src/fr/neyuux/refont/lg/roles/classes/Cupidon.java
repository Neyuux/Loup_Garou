package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class Cupidon extends Role {

    @Override
    public String getDisplayName() {
        return "§9§lCupi§d§ldon";
    }

    @Override
    public String getConfigName() {
        return "Cupidon";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous pouvez §9sélectionner 2 joueurs§f pour qu'ils deviennent le §d§lCouple§f de cette partie. Ils devront gagner ensemble ou avec leur camp d'origine (s'ils sont ensemble) ; et si l'un d'entre eux meurt, l'autre se suicidera d'un chagrin d'amour.";
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
        return "§fVous avez §d" + this.getTimeout() + " secondes §f pour choisir le couple de cette partie.";
    } //TODO prevenir si cupiEnCoupleOn et coupleRandomOn

    

}
