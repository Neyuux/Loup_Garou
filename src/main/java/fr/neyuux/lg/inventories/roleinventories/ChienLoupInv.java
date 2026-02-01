package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.items.menus.roleinventories.ChienLoupDogChoiceItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ChienLoupWolfChoiceItemStack;
import fr.neyuux.lg.roles.classes.ChienLoup;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChienLoupInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE1 = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)5).setDisplayName("§f"));
    public static final ClickableItem GLASS_PANE2 = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14).setDisplayName("§f"));


    private final Runnable callback;

    public ChienLoupInv(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE1);
        contents.set(0, 1, GLASS_PANE1);
        contents.set(0, 7, GLASS_PANE2);
        contents.set(0, 8, GLASS_PANE2);

        contents.set(1, 0, GLASS_PANE1);
        contents.set(1, 8, GLASS_PANE2);

        contents.set(2, 0, GLASS_PANE1);
        contents.set(2, 1, GLASS_PANE1);
        contents.set(2, 7, GLASS_PANE2);
        contents.set(2, 8, GLASS_PANE2);

        ChienLoupDogChoiceItemStack dogChoiceItemStack = new ChienLoupDogChoiceItemStack(callback);
        contents.set(1, 2, ClickableItem.of(dogChoiceItemStack, ev -> dogChoiceItemStack.use(ev.getWhoClicked(), ev)));

        ChienLoupWolfChoiceItemStack wolfChoiceItemStack = new ChienLoupWolfChoiceItemStack(callback);
        contents.set(1, 6, ClickableItem.of(wolfChoiceItemStack, ev -> wolfChoiceItemStack.use(ev.getWhoClicked(), ev)));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static SmartInventory getInventory(ChienLoup chienLoup, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_chienloup")
                .provider(new ChienLoupInv(callback))
                .size(3, 9)
                .title(chienLoup.getDisplayName())
                .closeable(false)
                .build();
    }
}
