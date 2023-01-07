package fr.neyuux.lg.items;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.items.hotbar.JoinGameEnderBallItemStack;
import fr.neyuux.lg.items.hotbar.OpComparatorItemStack;
import fr.neyuux.lg.items.hotbar.SpectatorTearItemStack;
import fr.neyuux.lg.items.hotbar.VoteBookItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemsManager {

    public void updateSpawnItems(PlayerLG playerLG) {
        PlayerInventory playerInv = playerLG.getPlayer().getInventory();

        this.clearInventory(playerLG);
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

        this.clearInventory(playerLG);
        /*if (playerLG.getRole() != null) {
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
        }*/
    }

    public void updateVoteItems(PlayerLG playerLG) {
        VoteLG vote = LG.getInstance().getGame().getVote();

        if (vote.getVoters().contains(playerLG)) {
            playerLG.getPlayer().getInventory().setItem(2, new VoteBookItemStack(vote));
        }
    }

    public void clearInventory(PlayerLG playerLG) {
        PlayerInventory playerInventory = playerLG.getPlayer().getInventory();

        playerInventory.clear();
        playerInventory.setArmorContents(null);
    }

    public void clearAllInventories() {
        for (PlayerLG playerLG : PlayerLG.getPlayersMap().values())
            this.clearInventory(playerLG);
    }

}
