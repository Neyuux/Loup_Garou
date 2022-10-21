package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.config.ComedianPowers;
import fr.neyuux.refont.lg.inventories.roleinventories.ChienLoupInv;
import fr.neyuux.refont.lg.inventories.roleinventories.ComedienInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Comedien extends Role {

    private final ArrayList<ComedianPowers> powers = new ArrayList<>();
    public boolean isInvOpen = false;


    @Override
    public String getDisplayName() {
        return "§5§lComédien";
    }

    @Override
    public String getConfigName() {
        return "Comédien";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous obtiendrez §9des pouvoirs de rôles villageois§f que vous pourrez utiliser une fois chacun pendant la partie.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d" + this.getTimeout() + " secondes §fpour choisir un rôle pour cette nuit.";
    }


    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        playerLG.sendMessage(LG.getPrefix() + "§dVos pouvoirs : ");
        for (ComedianPowers comedianPower : (ArrayList<ComedianPowers>)LG.getInstance().getGame().getConfig().getComedianPowers().getValue()) {
            powers.add(comedianPower);
            playerLG.sendMessage("§d - §a§l" + comedianPower.getName());
        }
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        new ComedienInv(this, playerLG, callback).open(playerLG.getPlayer());
        this.isInvOpen = true;
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        if (isInvOpen) playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
        this.isInvOpen = false;
        super.onPlayerTurnFinish(playerLG);
    }


    @EventHandler
    public void onCloseComedianInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();

        if (inv.getName().equals(this.getDisplayName()) && this.isInvOpen)
            ev.getPlayer().openInventory(inv);
    }
}
