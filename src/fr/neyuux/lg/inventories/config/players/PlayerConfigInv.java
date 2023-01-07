package fr.neyuux.lg.inventories.config.players;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.players.PlayerConfigItemStack;
import fr.neyuux.lg.items.menus.config.players.SetPlayerToSpectatorItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class PlayerConfigInv extends AbstractCustomInventory {

    private final PlayerLG playerLG;

    public PlayerConfigInv(PlayerLG playerLG) {
        super("§6§lMenu §b" + playerLG.getName(), 27);
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)1);
        this.setItem(26, new ReturnArrowItemStack(new ListPlayersInv()));
        this.setItem(4, new PlayerConfigItemStack(playerLG));

        this.setItem(12, new SetPlayerToSpectatorItemStack(playerLG));
    }
}
