package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.DictateurDoRebellionItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.VoleurGetNewRoleItemStack;
import fr.neyuux.refont.lg.roles.Camps;
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

        this.setItem(11, new VoleurGetNewRoleItemStack(callback, voleur.role1, voleur));
        this.setItem(13, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[] {"§bVous pouvez choisir de garder le rôle " + voleur.getDisplayName(), "§bet de supprimer les rôles " + voleur.role1.getDisplayName(), "§bet " + voleur.role2.getDisplayName() +" §bde la partie.", "", "§7>>Clique pour choisir"};
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                playerLG.setCamp(Camps.VILLAGE);
                playerLG.getCache().put("unclosableInv", false);
                playerLG.getPlayer().closeInventory();
                playerLG.setSleep();
                callback.run();
            }
        }));
        this.setItem(15, new VoleurGetNewRoleItemStack(callback, voleur.role2, voleur));
    }
}
