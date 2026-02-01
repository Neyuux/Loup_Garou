package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.VoleurInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import lombok.Getter;

public class Voleur extends Role {

    @Getter
    private static Role role1 = null;
    @Getter
    private static Role role2 = null;

    @Override
    public String getDisplayName() {
        return "§3§lVoleur";
    }

    @Override
    public String getConfigName() {
        return "Voleur";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, au début de la partie, vous allez devoir §9choisir §fentre les deux rôles qui n'ont pas été distribué (ou en choisir aucun)...";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §3" + this.getTimeout() + " secondes§f pour choisir votre rôle.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        super.onPlayerNightTurn(playerLG, callback);
        VoleurInv.getInventory(this, playerLG, callback).open(playerLG.getPlayer());
        
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    public static void setRole1(Role role1) {
        Voleur.role1 = role1;
    }

    public static void setRole2(Role role2) {
        Voleur.role2 = role2;
    }
    
    public static void removeRoles() {
        Voleur.setRole1(null);
        Voleur.setRole2(null);
    }
}
