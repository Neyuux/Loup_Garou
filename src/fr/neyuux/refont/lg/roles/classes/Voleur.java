package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.WinCamps;
import fr.neyuux.refont.lg.inventories.roleinventories.DictateurInv;
import fr.neyuux.refont.lg.inventories.roleinventories.VoleurInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Voleur extends Role {

    public Role role1;
    public Role role2;


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
        new VoleurInv(this, playerLG, callback).open(playerLG.getPlayer());
        playerLG.getCache().put("unclosableInv", true);
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseVoleurInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();

        if (inv.getName().equals(this.getDisplayName()) && (boolean)PlayerLG.createPlayerLG(player).getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(inv);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

}
