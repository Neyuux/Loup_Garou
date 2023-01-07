package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.PyromaneKillItemStack;
import fr.neyuux.lg.items.menus.roleinventories.PyromanePutOilItemStack;
import fr.neyuux.lg.roles.classes.Pyromane;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class PyromaneInv extends AbstractCustomInventory {

    private final Pyromane pyromane;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public PyromaneInv(Pyromane pyromane, PlayerLG playerLG, Runnable callback) {
        super(pyromane.getDisplayName(), 27);
        this.pyromane = pyromane;
        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new PyromanePutOilItemStack(pyromane, callback, this));
        this.setItem(13, new PyromaneKillItemStack(pyromane, callback));
        this.setItem(15, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
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
    }
}
