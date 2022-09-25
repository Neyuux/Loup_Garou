package fr.neyuux.refont.lg.inventories.config.players;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.players.PlayerConfigItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ListPlayersInv extends AbstractCustomInventory {

    public ListPlayersInv() {
        super("§6§lJoueurs", 36);
        this.adaptIntToInvSize(PlayerLG.getPlayersMap().size() + 18);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)1);
        this.setItem(this.getSize() - 1, new ReturnArrowItemStack(new ConfigurationInv()));

        for (PlayerLG playerLG : PlayerLG.getPlayersMap().values())
            for (int slot = 10; slot < this.getSize() - 9; slot++)
                if (this.getItem(slot) == null) {

                    this.setItem(slot, new PlayerConfigItemStack(playerLG));
                    return;
                }
    }
}
