package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Fossoyeur extends Role {

    @Override
    public String getDisplayName() {
        return "§8§lFossoyeur";
    }

    @Override
    public String getConfigName() {
        return "Fossoyeur";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). A votre mort, vous pourrez choisir un joueur ; et dans le chat, sera indiqué le pseudo de ce joueur et le pseudo d'un autre joueur d'un §9camp différent§f du premier.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WOLFY;
    }

    @Override
    public int getTimeout() {
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §3" + this.getTimeout() + " secondes §fpour choisir la personne à qui vous voulez creuser la tombe.";
    }
}
