package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.InfectPereDesLoupsInfectItemStack;
import fr.neyuux.lg.roles.classes.InfectPereDesLoups;
import fr.neyuux.lg.roles.classes.LoupGarou;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class InfectPereDesLoupsInv extends AbstractCustomInventory {

    private final Runnable callback;
    private final PlayerLG playerLG;

    public InfectPereDesLoupsInv(InfectPereDesLoups infectPereDesLoups, PlayerLG playerLG, Runnable callback) {
        super(infectPereDesLoups.getDisplayName(), 27);

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new InfectPereDesLoupsInfectItemStack(callback, LoupGarou.getLastTargetedByLG()));
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
