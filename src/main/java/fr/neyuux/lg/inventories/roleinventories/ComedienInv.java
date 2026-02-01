package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ComedianPowerChoiceItemStack;
import fr.neyuux.lg.roles.classes.Comedien;
import org.bukkit.entity.Player;

public class ComedienInv implements InventoryProvider {

    private final Runnable callback;
    private final Comedien comedian;
    private final PlayerLG playerLG;

    public ComedienInv(Comedien comedien, PlayerLG playerLG, Runnable callback) {
        this.callback = callback;
        this.comedian = comedien;
        this.playerLG = playerLG;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
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

        contents.set(LG.adaptIntToInvSize(comedian.getRemaningPowers().size()), 8, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        for (int i = 0; i < comedian.getRemaningPowers().size(); i++) {
            ComedianPowerChoiceItemStack choice = new ComedianPowerChoiceItemStack(comedian.getRemaningPowers().get(i), callback);
            contents.set(i / 9, i % 9, ClickableItem.of(choice, ev -> choice.use(ev.getWhoClicked(), ev)));
        }
    }


    public static SmartInventory getInventory(Comedien comedien, PlayerLG playerLG, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_comedien")
                .provider(new ComedienInv(comedien, playerLG, callback))
                .size(LG.adaptIntToInvSize(comedien.getRemaningPowers().size()), 9)
                .title(comedien.getDisplayName())
                .closeable(false)
                .build();
    }
}
