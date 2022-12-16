package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.DictateurDoRebellionItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.VoleurGetNewRoleItemStack;
import fr.neyuux.refont.lg.roles.classes.Dictateur;
import fr.neyuux.refont.lg.roles.classes.Voleur;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class VoleurInv extends AbstractCustomInventory {

    private final Voleur voleur;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public VoleurInv(Voleur voleur, PlayerLG playerLG, Runnable callback) {
        super(voleur.getDisplayName(), 27);
        this.voleur = voleur;

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)9);

        this.setItem(1, new VoleurGetNewRoleItemStack(callback));
        this.setItem(13, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[] {"§bVous pouvez choisir de garder le rôle " + voleur.getDisplayName(), "§bet de supprimer les rôles " + voleur.role1.getDisplayName(), "§bet " + voleur.role2.getDisplayName() +" §bde la partie.", "", "§7>>Clique pour choisir"};
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
