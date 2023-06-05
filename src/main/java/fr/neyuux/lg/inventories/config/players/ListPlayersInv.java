package fr.neyuux.lg.inventories.config.players;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.config.ChangeGameTypeInv;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.inventories.config.ResetInv;
import fr.neyuux.lg.inventories.config.parameters.ParametersInv;
import fr.neyuux.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.lg.items.PaginationNextItemStack;
import fr.neyuux.lg.items.PaginationPreviousItemStack;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.players.PlayerConfigItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListPlayersInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)1).setDisplayName("§f"));

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("config_players_main_inv")
            .provider(new ListPlayersInv())
            .size(6, 9)
            .title("§6§lJoueurs")
            .closeable(true)
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(4, 0, GLASS_PANE);
        contents.set(4, 8, GLASS_PANE);

        contents.set(5, 0, GLASS_PANE);
        contents.set(5, 1, GLASS_PANE);
        contents.set(5, 7, GLASS_PANE);
        contents.set(5, 8, GLASS_PANE);

        final Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(7 * 4);

        contents.set(5, 4, ClickableItem.of(new ReturnArrowItemStack(), onClick -> onClick.getWhoClicked().closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        final Pagination pagination = contents.pagination();
        final List<ClickableItem> items = new ArrayList<>();

        PlayerLG.getPlayersMap().values()
                .stream()
                .map(playerLG -> ClickableItem.of(new CustomItemStack(Material.SKULL_ITEM)
                        .setDisplayName(playerLG.getDisplayName())
                        .addLore("")
                        .addLore("§7Spectateur » " + LG.getStringBoolean(playerLG.isSpectator()))
                        .addLore("§aOP » " + LG.getStringBoolean(playerLG.isOP()))
                        .addLore("")
                        .addLore("§7>>Clique pour gérer ce joueur")
                        .setSkullOwner(playerLG.getName())
                        , onClick -> getPlayerInventory(playerLG).open(player)))
                .forEach(items::add);

        pagination.setItems(items.toArray(new ClickableItem[0]));

        final SlotIterator iterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1);

        //blacklist border's slots
        for (int i = 1; i < INVENTORY.getRows() - 1; i++) {
            iterator.blacklist(i, 0);
            iterator.blacklist(i, 8);
        }

        pagination.addToIterator(iterator);

        if (!pagination.isFirst())
            contents.set(5, 2, ClickableItem.of(new PaginationPreviousItemStack(), onClick -> INVENTORY.open((Player) onClick.getWhoClicked(), pagination.previous().getPage()))); //previous page

        if (!pagination.isLast())
            contents.set(5, 6, ClickableItem.of(new PaginationNextItemStack(), onClick -> INVENTORY.open((Player) onClick.getWhoClicked(), pagination.next().getPage()))); //next page

    }


    private static SmartInventory getPlayerInventory(PlayerLG playerLG) {
        return SmartInventory.builder()
                .id("config_players_"+playerLG.getPlayer().getUniqueId()+"_inv")
                .provider(new PlayerConfigInv(playerLG))
                .size(3, 9)
                .title("§6§lMenu §b" + playerLG.getName())
                .closeable(true)
                .build();
    }
}

class PlayerConfigInv implements InventoryProvider {

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
}
