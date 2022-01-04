package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class ItemsManager {

    private final GameLG gameLG;

    public ItemsManager(GameLG gameLG) {
        this.gameLG = gameLG;
    }


    public void updateSpawnItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();
        playerInv.clear();
        playerInv.setItem(1, );
    }

}
