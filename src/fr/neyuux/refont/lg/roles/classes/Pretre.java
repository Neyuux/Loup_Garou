package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Pretre extends Role {

    public Pretre(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§e§lPrêtre";
    }

    @Override
    public String getScoreboardName() {
        return "§e§lPrêtre";
    }

    @Override
    public String getConfigName() {
        return "Pretre";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous possédez une fiole d'eau bénite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il §9mourra§f sinon, vous mourrez.";
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
        return "§fVous avez §e" + this.getTimeout() + " secondes §fpour lancer de l'eau bénite sur quelqu'un.";
    }
}
