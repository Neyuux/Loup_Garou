package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.DictateurDoRebellionItemStack;
import fr.neyuux.refont.lg.roles.classes.Dictateur;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class DictateurInv extends AbstractCustomInventory {

    private final Runnable callback;
    private final PlayerLG playerLG;

    public DictateurInv(Dictateur dictateur, PlayerLG playerLG, Runnable callback) {
        super(dictateur.getDisplayName(), 27);

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new DictateurDoRebellionItemStack(callback));
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
