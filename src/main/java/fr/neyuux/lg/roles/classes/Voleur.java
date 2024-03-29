package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.VoleurInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Voleur extends Role {

    private static Role role1 = null;
    private static Role role2 = null;

    @Override
    public String getDisplayName() {
        return "�3�lVoleur";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, au d�but de la partie, vous allez devoir �9choisir �fentre les deux r�les qui n'ont pas �t� distribu� (ou en choisir aucun)...";
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
        return "�fVous avez �3" + this.getTimeout() + " secondes�f pour choisir votre r�le.";
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
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }


    @EventHandler
    public void onCloseVoleurInv(InventoryCloseEvent ev) {
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


    public static Role getRole1() {
        return role1;
    }

    public static Role getRole2() {
        return role2;
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
