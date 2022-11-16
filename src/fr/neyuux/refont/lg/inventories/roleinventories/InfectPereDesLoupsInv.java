package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.InfectPereDesLoupsInfectItemStack;
import fr.neyuux.refont.lg.roles.classes.InfectPereDesLoups;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class InfectPereDesLoupsInv extends AbstractCustomInventory {

    private final Runnable callback;
    private final PlayerLG playerLG;
    private final InfectPereDesLoups infectPereDesLoups;

    public InfectPereDesLoupsInv(fr.neyuux.refont.lg.roles.classes.InfectPereDesLoups infectPereDesLoups, PlayerLG playerLG, Runnable callback) {
        super(infectPereDesLoups.getDisplayName(), 27);

        this.callback = callback;
        this.playerLG = playerLG;
        this.infectPereDesLoups = infectPereDesLoups;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new InfectPereDesLoupsInfectItemStack(callback, infectPereDesLoups.getLastTargetedByLG()));
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
