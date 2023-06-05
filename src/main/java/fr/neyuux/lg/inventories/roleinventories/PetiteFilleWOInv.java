package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.PetiteFilleWOInspectionItemStack;
import fr.neyuux.lg.roles.classes.PetiteFilleWO;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;

public class PetiteFilleWOInv extends AbstractCustomInventory {

    private final Runnable callback;
    private final PlayerLG playerLG;

    public PetiteFilleWOInv(PetiteFilleWO petiteFilleWO, PlayerLG playerLG, Runnable callback) {
        super(petiteFilleWO.getDisplayName(), 27);

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new PetiteFilleWOInspectionItemStack(callback));
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

        if (LG.getInstance().getGame().getLGs(true).size() == 0) {
            this.setItem(11, new CustomItemStack(Material.BARRIER, 1, "§c§lPlus de Loups-Garou").addLore("§fIl n'y a plus de Loups à trouver.").addGlowEffect());
        }
    }
}
