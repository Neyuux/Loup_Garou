package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class LoupGarouBlanc extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lLoup-Garou §f§lBlanc";
    }

    @Override
    public String getConfigName() {
        return "Loup-Garou Blanc";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est de §9terminer la partie seul§f. Pour les autres §c§lLoups-Garous§f, vous apparaissez comme leur coéquipier : attention à ne pas être découvert...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
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
        return "§fVous avez §4" + this.getTimeout() + " secondes §fpour choisir de tuer un Loup-Garou.";
    }
}
