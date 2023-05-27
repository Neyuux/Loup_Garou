package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.VoleurGetNewRoleItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.classes.Voleur;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import org.bukkit.Bukkit;

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

        this.setItem(11, new VoleurGetNewRoleItemStack(callback, Voleur.getRole1(), Voleur.getRole2(), voleur));
        this.setItem(13, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[] {"�bVous pouvez choisir de garder le r�le " + voleur.getDisplayName(), "�bet de supprimer les r�les " + Voleur.getRole1().getDisplayName(), "�bet " + Voleur.getRole2().getDisplayName() +" �bde la partie.", "", "�7>>Clique pour choisir"};
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
        this.setItem(15, new VoleurGetNewRoleItemStack(callback, Voleur.getRole2(), Voleur.getRole1(), voleur));
    }
}
