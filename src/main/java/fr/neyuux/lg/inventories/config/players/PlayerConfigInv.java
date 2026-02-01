package fr.neyuux.lg.inventories.config.players;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerConfigInv implements InventoryProvider {

    private static final ClickableItem GLASS_PANE = ListPlayersInv.GLASS_PANE;
    private final PlayerLG playerLG;

    public PlayerConfigInv(PlayerLG playerLG) {
        this.playerLG = playerLG;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(2, 0, GLASS_PANE);
        contents.set(2, 1, GLASS_PANE);
        contents.set(2, 7, GLASS_PANE);
        contents.set(2, 8, GLASS_PANE);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        contents.set(0, 4, ClickableItem.empty(new CustomItemStack(Material.SKULL_ITEM)
                .setDisplayName(playerLG.getDisplayName())
                .addLore("")
                .addLore("§7Spectateur » " + LG.getStringBoolean(playerLG.isSpectator()))
                .addLore("§aOP » " + LG.getStringBoolean(playerLG.isOP()))
                .addLore("")
                .addLore("§7>>Clique pour gérer ce joueur")
                .setSkullOwner(playerLG.getName())));

        contents.set(1, 3, ClickableItem.of(new CustomItemStack(Material.GHAST_TEAR)
                .setDisplayName("§fMettre §b" + playerLG.getName() + " §fen §7Spectateur§f.")
                .setLore("", "Actuel » " + (playerLG.isSpectator() ? "§aOui" : "§cNon"), "", "§7>>Clique pour modifier")
                , onClick -> {
                    if (playerLG.isSpectator()) {
                        LG.getInstance().getGame().removeSpectator(playerLG);

                        playerLG.sendMessage(LG.getPrefix() + player.getDisplayName() + " §9vous a retiré du mode §7§lSpectateur§9.");
                        player.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §cretiré§7 du mode §lSpectateur§7.");
                    } else {
                        LG.getInstance().getGame().setSpectator(playerLG);

                        player.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §amit§7 en §lSpectateur§7.");
                        playerLG.sendMessage(LG.getPrefix() + "§b" + player.getDisplayName() + "§7 vous a mis en mode §lSpectateur§7.");
                    }
                }));
    }

    public static SmartInventory getPlayerInventory(PlayerLG playerLG) {
        return SmartInventory.builder()
                .id("config_players_"+playerLG.getPlayer().getUniqueId()+"_inv")
                .provider(new PlayerConfigInv(playerLG))
                .size(3, 9)
                .title("§6§lMenu §b" + playerLG.getName())
                .closeable(true)
                .build();
    }
}
