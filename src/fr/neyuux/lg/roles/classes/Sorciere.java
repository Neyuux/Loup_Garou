package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.SorciereInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Sorciere extends Role {

    private boolean hasHealPot = true;
    private boolean hasKillPot = true;
    private BukkitTask checkInventory;


    @Override
    public String getDisplayName() {
        return "�5�lSorci�re";
    }

    @Override
    public String getConfigName() {
        return "Sorciere";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Vous poss�dez 2 �2potions�f : une �apotion de vie�f�o(qui vous permettera de r�ssuciter un joueur, dont le nom vous sera donn�, d�vor� par les Loups)�f et une �4potion de mort�f�o(qui vous permettera de tuer un joueur de votre choix)�f.";
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
        return "�fVous avez �5" + this.getTimeout() + " secondes�f pour utiliser vos potions.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        playerLG.getPlayer().openInventory(new SorciereInv(this, playerLG, callback).getInventory());

        this.checkInventory = new BukkitRunnable() {
            @Override
            public void run() {
                Player player = playerLG.getPlayer();

                if (player.getOpenInventory() == null || player.getOpenInventory().getTopInventory() == null)
                    player.openInventory(new SorciereInv(Sorciere.this, playerLG, callback).getInventory());
            }
        }.runTaskTimer(LG.getInstance(), 0L, 5L);
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        this.checkInventory.cancel();

        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }


    public boolean hasHealPot() {
        return hasHealPot;
    }

    public boolean hasKillPot() {
        return hasKillPot;
    }


    public void setHealPot(boolean hasHealPot) {
        this.hasHealPot = hasHealPot;
    }

    public void setKillPot(boolean hasKillPot) {
        this.hasKillPot = hasKillPot;
    }
}
