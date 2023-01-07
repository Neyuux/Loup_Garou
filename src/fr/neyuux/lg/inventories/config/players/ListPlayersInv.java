package fr.neyuux.lg.inventories.config.players;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.players.PlayerConfigItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

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
