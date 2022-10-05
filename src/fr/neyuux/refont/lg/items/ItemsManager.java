package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.hotbar.JoinGameEnderBallItemStack;
import fr.neyuux.refont.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.refont.lg.items.hotbar.SpectatorTearItemStack;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemsManager {

    public void updateSpawnItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();

        playerInv.clear();
        if (!playerLG.isSpectator() && LG.getInstance().getGame().getGameState().equals(GameState.WAITING)) {
            playerInv.setItem(1, new SpectatorTearItemStack());
            if (!playerLG.isInGame()) playerInv.setItem(5, new JoinGameEnderBallItemStack());
        }
        if (playerLG.isOP() && LG.getInstance().getGame().getGameState().equals(GameState.WAITING)) {
            playerInv.setItem(6, new OpComparatorItemStack());
        }
    }

    public void updateStartItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();

        playerInv.clear();
        if (playerLG.getRole() != null) {
            ItemStack it = null;
            ItemMeta itm = null;
            List<Block> chests = new ArrayList<>();
            chests.add(Bukkit.getWorld("LG").getBlockAt(442, 11, 510));
            chests.add(Bukkit.getWorld("LG").getBlockAt(439, 11, 510));

            for (Block block : chests) {
                org.bukkit.block.Chest chest = (org.bukkit.block.Chest)  block.getState();
                Inventory inventory = chest.getInventory();

                for (ItemStack its : inventory.getContents())
                    if (its != null)
                        if (its.hasItemMeta())
                            if (its.getItemMeta().getDisplayName().equalsIgnoreCase(playerLG.getRole().getConfigName())) {

                                it = new ItemStack(its.getType(), its.getAmount(), its.getDurability());
                                itm = its.getItemMeta();
                            }
            }

            if (it == null) {
                Bukkit.broadcastMessage(LG.getPrefix() + "§6[§eAvertissement§6] §eImpossible de récuperer la carte de " + playerLG.getName());
                Bukkit.getLogger().warning("could not get map for role " + playerLG.getRole().getConfigName());
                return;
            }
            it.setItemMeta(itm);
            playerInv.setItem(4, it);
        }
    }

}
