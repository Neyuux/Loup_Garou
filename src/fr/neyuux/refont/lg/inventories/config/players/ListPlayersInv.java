package fr.neyuux.refont.lg.inventories.config.players;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.players.PlayerConfigItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ListPlayersInv extends AbstractCustomInventory {

    public ListPlayersInv() {
        super("§6§lJoueurs", 36, 4);
        this.adaptIntToInvSize(PlayerLG.getPlayersMap().size());
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() -1, new ReturnArrowItemStack(new ConfigurationInv()));
        this.setAllCorners((byte)1);

        for (PlayerLG playerLG : PlayerLG.getPlayersMap().values()) this.addItem(new PlayerConfigItemStack(playerLG));
    }
}
