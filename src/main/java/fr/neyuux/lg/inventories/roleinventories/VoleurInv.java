package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.VoleurGetNewRoleItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.classes.Voleur;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VoleurInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)9).setDisplayName("§f"));


    private final Voleur voleur;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public VoleurInv(Voleur voleur, PlayerLG playerLG, Runnable callback) {
        this.voleur = voleur;

        this.callback = callback;
        this.playerLG = playerLG;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(2, 0, GLASS_PANE);
        contents.set(2, 1, GLASS_PANE);
        contents.set(2, 7, GLASS_PANE);
        contents.set(2, 8, GLASS_PANE);

        CancelBarrierItemStack cancel = new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[] {"§bVous pouvez choisir de garder le rôle " + voleur.getDisplayName(), "§bet de supprimer les rôles " + Voleur.getRole1().getDisplayName(), "§bet " + Voleur.getRole2().getDisplayName() +" §bde la partie.", "", "§7>>Clique pour choisir"};
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                playerLG.setCamp(Camps.VILLAGE);
                
                LG.closeSmartInv(playerLG.getPlayer());
                playerLG.setSleep();
                callback.run();
            }
        });

        contents.set(1, 4, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));


        VoleurGetNewRoleItemStack voleuritem1 = new VoleurGetNewRoleItemStack(callback, Voleur.getRole1(), Voleur.getRole2(), voleur);
        VoleurGetNewRoleItemStack voleuritem2 = new VoleurGetNewRoleItemStack(callback, Voleur.getRole2(), Voleur.getRole1(), voleur);

        contents.set(1, 2, ClickableItem.of(voleuritem1, ev -> {
            voleuritem1.use(ev.getWhoClicked(),  ev);
        }));

        contents.set(1, 6, ClickableItem.of(voleuritem2, ev -> {
            voleuritem2.use(ev.getWhoClicked(),  ev);
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static SmartInventory getInventory(Voleur voleur, PlayerLG playerLG, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_voleur")
                .provider(new VoleurInv(voleur, playerLG, callback))
                .size(3, 9)
                .title(voleur.getDisplayName())
                .closeable(false)
                .build();
    }
}
