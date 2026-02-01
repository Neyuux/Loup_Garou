package fr.neyuux.lg.inventories.config;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.reset.NoResetMapItemStack;
import fr.neyuux.lg.items.menus.config.reset.YesResetMapItemStack;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ResetInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3).setDisplayName("§f"));


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

        YesResetMapItemStack yes = new YesResetMapItemStack();
        contents.set(1, 2, ClickableItem.of(yes, ev -> yes.use(ev.getWhoClicked(), ev)));

        NoResetMapItemStack no = new NoResetMapItemStack();
        contents.set(1, 3, ClickableItem.of(no, ev -> no.use(ev.getWhoClicked(), ev)));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("reset")
            .provider(new ResetInv())
            .size(3, 9)
            .title("§b§lReset §bla Map")
            .closeable(true)
            .build();
}
