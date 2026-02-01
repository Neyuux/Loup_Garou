package fr.neyuux.lg.inventories.config.players;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.PaginationNextItemStack;
import fr.neyuux.lg.items.PaginationPreviousItemStack;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
                        , onClick -> PlayerConfigInv.getPlayerInventory(playerLG).open(player)))
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

}

