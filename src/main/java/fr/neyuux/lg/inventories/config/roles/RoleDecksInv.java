package fr.neyuux.lg.inventories.config.roles;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.roles.DecksItemStack;
import fr.neyuux.lg.roles.Decks;
import org.bukkit.entity.Player;

public class RoleDecksInv implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> ConfigurationInv.INVENTORY.open((Player) ev.getWhoClicked())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        for (int i = 0; i < Decks.values().length; i++) {
            DecksItemStack decksItemStack = new DecksItemStack(Decks.values()[i]);
            contents.set(0, i, ClickableItem.of(decksItemStack, ev -> decksItemStack.use(ev.getWhoClicked(), ev)));
        }
    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("roles_decks")
            .provider(new RoleDecksInv())
            .size(1, 9)
            .title("§6§lMenu §bDecks")
            .closeable(true)
            .build();
}
