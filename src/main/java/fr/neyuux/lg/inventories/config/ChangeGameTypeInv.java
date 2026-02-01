package fr.neyuux.lg.inventories.config;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.changegametypeinv.ChangeGameTypeToFreeItemStack;
import fr.neyuux.lg.items.menus.config.changegametypeinv.ChangeGameTypeToMeetingItemStack;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChangeGameTypeInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)13).setDisplayName("§f"));


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

        contents.set(2, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> ConfigurationInv.INVENTORY.open((Player) ev.getWhoClicked())));

        ChangeGameTypeToFreeItemStack freeItemStack = new ChangeGameTypeToFreeItemStack();
        contents.set(1, 2, ClickableItem.of(freeItemStack, ev -> freeItemStack.use(ev.getWhoClicked(), ev)));

        ChangeGameTypeToMeetingItemStack meetingItemStack = new ChangeGameTypeToMeetingItemStack();
        contents.set(1, 6, ClickableItem.of(meetingItemStack, ev -> meetingItemStack.use(ev.getWhoClicked(), ev)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("gametype")
            .provider(new ChangeGameTypeInv())
            .size(3, 9)
            .title("§2§lType §cde la Partie")
            .closeable(true)
            .build();
}
