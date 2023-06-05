package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.config.ComedianPowers;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ComedianPowerChoiceItemStack;
import fr.neyuux.lg.roles.classes.Comedien;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ComedienInv extends AbstractCustomInventory {

    private final Runnable callback;
    private final Comedien comedian;
    private final PlayerLG playerLG;

    public ComedienInv(Comedien comedien, PlayerLG playerLG, Runnable callback) {
        super(comedien.getDisplayName(), 36);
        this.adaptIntToInvSize(comedien.getRemaningPowers().size());

        this.callback = callback;
        this.comedian = comedien;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
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

        for (ComedianPowers power : comedian.getRemaningPowers())
            this.addItem(new ComedianPowerChoiceItemStack(power, callback));
    }
}
