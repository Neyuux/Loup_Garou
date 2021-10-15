package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Enchanteur extends Role {

    public Enchanteur(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§c§lEnchanteur";
    }

    @Override
    public String getScoreboardName() {
        return "§c§lEnchanteur";
    }

    @Override
    public String getConfigName() {
        return "Enchanteur";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Vous ne connaissez pas les autres Loups. Chaque nuit, vous pourrez enchanter un joueur et découvrir s'il agit d'une voyante ou un Loup.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.LOUP_GAROU;
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
        return "Vous avez §c" + this.getTimeout() + " secondes§f pour apprendre le camp d'un joueur.";
    }

}
