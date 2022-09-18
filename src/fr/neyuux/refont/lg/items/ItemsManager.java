package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.hotbar.JoinGameEnderBallItemStack;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.items.hotbar.SpectatorTearItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemsManager {

    public void updateSpawnItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();

        playerInv.clear();
        if (!playerLG.isSpectator()) {
            playerInv.setItem(1, new SpectatorTearItemStack());
            playerInv.setItem(5, new JoinGameEnderBallItemStack());
        }
        if (playerLG.isOP()) {
            playerInv.setItem(6, new OpComparatorItemStack());
        }
    }

}
