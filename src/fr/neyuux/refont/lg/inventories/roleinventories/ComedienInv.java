package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.config.ComedianPowers;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.ComedianPowerChoiceItemStack;
import fr.neyuux.refont.lg.roles.classes.Comedien;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

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
