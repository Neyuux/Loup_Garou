package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereKillItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereKilledItemStack;
import fr.neyuux.lg.items.menus.roleinventories.SorciereReviveItemStack;
import fr.neyuux.lg.roles.classes.LoupGarou;
import fr.neyuux.lg.roles.classes.Sorciere;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class SorciereInv implements InventoryProvider {

    private final Sorciere sorciere;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public SorciereInv(Sorciere sorciere, PlayerLG playerLG, Runnable callback) {
        this.sorciere = sorciere;
        this.callback = callback;
        this.playerLG = playerLG;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        SorciereKilledItemStack killedItemStack = new SorciereKilledItemStack(playerLG);
        CancelBarrierItemStack cancel = new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                
                LG.closeSmartInv(playerLG.getPlayer());
                playerLG.setSleep();
                callback.run();
            }
        });

        contents.set(0, 1, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));

        contents.set(0, 3, ClickableItem.of(killedItemStack, ev -> killedItemStack.use(ev.getWhoClicked(), ev)));


        if (LoupGarou.getLastTargetedByLG() != null) {
            if (this.sorciere.hasHealPot()) {
                SorciereReviveItemStack reviveitem = new SorciereReviveItemStack(callback, playerLG, sorciere);

                contents.set(0, 0, ClickableItem.of(reviveitem, ev -> reviveitem.use(ev.getWhoClicked(), ev)));

            } else {
                CustomItemStack noreviveitem = new CustomItemStack(Material.BARRIER).setDisplayName("§cPlus de potion").setLore("§7Vous n'avez plus de potions de vie", "§7car vous avez déjà réssucité.");

                contents.set(0, 0, ClickableItem.of(noreviveitem, ev -> noreviveitem.use(ev.getWhoClicked(), ev)));

            }
        }

        if (this.sorciere.hasKillPot()) {
            SorciereKillItemStack killitem = new SorciereKillItemStack(callback, playerLG, sorciere);

            contents.set(0, 2, ClickableItem.of(killitem, ev -> killitem.use(ev.getWhoClicked(), ev)));

        } else {
            CustomItemStack nokillitem = new CustomItemStack(Material.BARRIER).setDisplayName("§cPlus de potion").setLore("§7Vous n'avez plus de potions de mort.", "§7car vous avez déjà tué quelqu'un.");

            contents.set(0, 2, ClickableItem.of(nokillitem, ev -> nokillitem.use(ev.getWhoClicked(), ev)));
        }


    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static SmartInventory getInventory(Sorciere sorciere, PlayerLG playerLG, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_sorciere")
                .provider(new SorciereInv(sorciere, playerLG, callback))
                .type(InventoryType.BREWING)
                .title(sorciere.getDisplayName())
                .closeable(false)
                .build();
    }
}
