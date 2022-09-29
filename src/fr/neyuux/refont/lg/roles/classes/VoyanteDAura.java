package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

public class VoyanteDAura extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lVoyante §4§ld'Aura";
    }

    @Override
    public String getConfigName() {
        return "Voyante d'Aura";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous vous réveillerez et découvrez si un joueur que vous choisirez est Loup ou non.";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §4" + this.getTimeout() + " secondes §fpour espionner l'aura de quelqu'un.";
    }
}
