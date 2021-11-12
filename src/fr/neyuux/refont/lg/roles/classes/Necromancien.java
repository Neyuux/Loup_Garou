package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Necromancien extends Role {

    public Necromancien(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§9§lNécromancien";
    }

    @Override
    public String getConfigName() {
        return "Nécromancien";
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez §9récussiter§f un joueur. S'il avait un pouvoir, il le perd et devient §e§lSimple §a§lVillageois§f.";
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
        return "§fVous avez §9" + this.getTimeout() +  " secondes§f pour réssuciter quelqu'un.";
    }
}
