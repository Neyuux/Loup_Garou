package fr.neyuux.lg.inventories.config.parameters;


import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.*;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ParametersGlobalInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)5).setDisplayName("§f"));


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

        contents.set(2, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> ParametersInv.INVENTORY.open((Player) ev.getWhoClicked())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        ParameterDayCycleItemStack cycleItemStack = new ParameterDayCycleItemStack();
        contents.set(1, 2, ClickableItem.of(cycleItemStack, ev -> cycleItemStack.use(ev.getWhoClicked(), ev)));

        ParameterLGChatItemStack lgChatItemStack = new ParameterLGChatItemStack();
        contents.set(1, 3, ClickableItem.of(lgChatItemStack, ev -> lgChatItemStack.use(ev.getWhoClicked(), ev)));

        ParameterMayorItemStack mayorItemStack = new ParameterMayorItemStack();
        contents.set(1, 4, ClickableItem.of(mayorItemStack, ev -> mayorItemStack.use(ev.getWhoClicked(), ev)));

        ParameterRandomCoupleItemStack randomCoupleItemStack = new ParameterRandomCoupleItemStack();
        contents.set(1, 5, ClickableItem.of(randomCoupleItemStack, ev -> randomCoupleItemStack.use(ev.getWhoClicked(), ev)));

        ParameterMayorSuccessionItemStack chamanChatItemStack = new ParameterMayorSuccessionItemStack();
        contents.set(1, 6, ClickableItem.of(chamanChatItemStack, ev -> chamanChatItemStack.use(ev.getWhoClicked(), ev)));
    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("config_global")
            .provider(new ParametersGlobalInv())
            .size(3, 9)
            .title("§f§lParamètres §a§lGlobaux")
            .closeable(true)
            .build();
}
