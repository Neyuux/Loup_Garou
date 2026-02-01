package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.InfectPereDesLoupsInfectItemStack;
import fr.neyuux.lg.roles.classes.InfectPereDesLoups;
import fr.neyuux.lg.roles.classes.LoupGarou;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InfectPereDesLoupsInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14).setDisplayName("§f"));


    private final InfectPereDesLoups infectPereDesLoups;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public InfectPereDesLoupsInv(InfectPereDesLoups infectPereDesLoups, PlayerLG playerLG, Runnable callback) {
        this.infectPereDesLoups = infectPereDesLoups;
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
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        InfectPereDesLoupsInfectItemStack infect = new InfectPereDesLoupsInfectItemStack(callback, LoupGarou.getLastTargetedByLG());
        contents.set(1, 2, ClickableItem.of(infect, ev -> {
            infect.use(ev.getWhoClicked(), ev);
            infectPereDesLoups .setInfect(true);
        }));

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
        contents.set(1, 6, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));

    }

    public static SmartInventory getInventory(InfectPereDesLoups infectPereDesLoups, PlayerLG playerLG, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_ipdl")
                .provider(new InfectPereDesLoupsInv(infectPereDesLoups, playerLG, callback))
                .size(3, 9)
                .title(infectPereDesLoups.getDisplayName())
                .closeable(false)
                .build();
    }
}
