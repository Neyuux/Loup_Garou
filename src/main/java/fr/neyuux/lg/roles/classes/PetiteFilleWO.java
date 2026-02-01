package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.PetiteFilleWOInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;

public class PetiteFilleWO extends Role {

    @Override
    public String getDisplayName() {
        return "§9§lPetite §b§lFille §0§oWO";
    }

    @Override
    public String getConfigName() {
        return "Petite Fille (WO)";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez ouvrir les yeux, si vous le faites, vous aurez 20% de chance de §9trouver un Loup§f et 5% de chance de §9mourir§f.";
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
        return "§fVous avez §b" + this.getTimeout() + " secondes§f pour choisir d'aller espionner ou non.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        super.onPlayerNightTurn(playerLG, callback);
        PetiteFilleWOInv.getInventory(this, playerLG, callback).open(playerLG.getPlayer());
        
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

}
