package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Sorciere extends Role {

    private boolean hasHealPot = true;
    private boolean hasKillPot = true;
    private PlayerLG healTarget;
    private PlayerLG killTarget;


    @Override
    public String getDisplayName() {
        return "§5§lSorcière";
    }

    @Override
    public String getConfigName() {
        return "Sorciere";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Vous possèdez 2 §2potions§f : une §apotion de vie§f§o(qui vous permettera de réssuciter un joueur, dont le nom vous sera donné, dévoré par les Loups)§f et une §4potion de mort§f§o(qui vous permettera de tuer un joueur de votre choix)§f.";
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
        return "§fVous avez §5" + this.getTimeout() + " secondes§f pour utiliser vos potions.";
    }


    public boolean hasHealPot() {
        return hasHealPot;
    }

    public boolean hasKillPot() {
        return hasKillPot;
    }

    public PlayerLG getHealTarget() {
        return healTarget;
    }

    public PlayerLG getKillTarget() {
        return killTarget;
    }

    public void setHealPot(boolean hasHealPot) {
        this.hasHealPot = hasHealPot;
    }

    public void setKillPot(boolean hasKillPot) {
        this.hasKillPot = hasKillPot;
    }

    public void setHealTarget(PlayerLG healTarget) {
        this.healTarget = healTarget;
    }

    public void setKillTarget(PlayerLG killTarget) {
        this.killTarget = killTarget;
    }
}
