package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereKillItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereKilledItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereReviveItemStack;
import fr.neyuux.lg.roles.classes.Sorciere;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class SorciereInv {

    private final Inventory inventory;
    private final Sorciere sorciere;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public SorciereInv(Sorciere sorciere, PlayerLG playerLG, Runnable callback) {
        this.inventory = Bukkit.createInventory(null, InventoryType.BREWING, sorciere.getDisplayName());
        this.sorciere = sorciere;
        this.callback = callback;
        this.playerLG = playerLG;
        this.registerItems();
    }

    public void registerItems() {
        inventory.setItem(3, new SorciereKilledItemStack(playerLG));
        inventory.setItem(1, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                playerLG.getCache().put("unclosableInv", false);
                playerLG.getPlayer().closeInventory();
                playerLG.setSleep();
                callback.run();
            }
        }));

        if (this.sorciere.hasHealPot()) inventory.setItem(0, new SorciereReviveItemStack(callback, playerLG, sorciere));
        else inventory.setItem(0, new CustomItemStack(Material.BARRIER).setDisplayName("§cPlus de potion").setLore("§7Vous n'avez plus de potions de vie", "§7car vous avez déjà réssucité."));

        if (this.sorciere.hasKillPot()) inventory.setItem(2, new SorciereKillItemStack(callback, playerLG, sorciere));
        else inventory.setItem(0, new CustomItemStack(Material.BARRIER).setDisplayName("§cPlus de potion").setLore("§7Vous n'avez plus de potions de mort.", "§7car vous avez déjà tué quelqu'un."));
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
