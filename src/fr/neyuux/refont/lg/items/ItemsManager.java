package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.hotbar.JoinGameEnderBallItemStack;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.items.hotbar.SpectatorTearItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemsManager {

    public void updateSpawnItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();

        playerInv.clear();
        if (!playerLG.isSpectator() && LG.getInstance().getGame().getGameState().equals(GameState.WAITING)) {
            playerInv.setItem(1, new SpectatorTearItemStack());
            if (!playerLG.isInGame()) playerInv.setItem(5, new JoinGameEnderBallItemStack());
        }
        if (playerLG.isOP() && LG.getInstance().getGame().getGameState().equals(GameState.PLAYING)) {
            playerInv.setItem(6, new OpComparatorItemStack());
        }
    }

}
