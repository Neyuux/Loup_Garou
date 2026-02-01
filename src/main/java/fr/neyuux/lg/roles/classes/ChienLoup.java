package fr.neyuux.lg.roles.classes;

import fr.minuskube.inv.SmartInventory;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.ChienLoupInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.entity.Player;

public class ChienLoup extends Role {

    @Override
    public String getDisplayName() {
        return "§a§lChien§e-§c§lLoup";
    }

    @Override
    public String getConfigName() {
        return "Chien-Loup";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, au début de la partie, vous allez devoir choisir entre devenir §c§lLoup-Garou §fou §e§lSimple §a§lVillageois§f.";
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
        return "§fVous avez §e" + getTimeout() + " secondes§f pour choisir votre camp.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        Player player = playerLG.getPlayer();
        SmartInventory inventory = ChienLoupInv.getInventory(this, callback);

        super.onPlayerNightTurn(playerLG, callback);

        inventory.open(player);
        //TODO bug onReset? others roles cannot play
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

}
