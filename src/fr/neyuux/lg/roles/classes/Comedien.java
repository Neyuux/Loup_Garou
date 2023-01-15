package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.config.ComedianPowers;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.inventories.roleinventories.ComedienInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Comedien extends Role {

    private final ArrayList<ComedianPowers> powers = new ArrayList<>();

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
        playerLG.getCache().put("unclosableInv", true);
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        if ((boolean)playerLG.getCache().get("unclosableInv")) playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
    }


    @EventHandler
    public void onCloseComedianInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().equals(this.getDisplayName()) && (boolean)playerLG.getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerLG.getCache().put("unclosableInv", false);
                    player.openInventory(inv);
                    playerLG.getCache().put("unclosableInv", true);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG.getCache().has("comedianpower")) {
                playerLG.setRole((Role) playerLG.getCache().get("comedianpower"));
                playerLG.getCache().remove("comedianpower");
            }
    }


    public ArrayList<ComedianPowers> getRemaningPowers() {
        return powers;
    }
}
