package fr.neyuux.lg.inventories.config.parameters;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ParametersGlobalItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ParametersRolesItemStack;
import org.bukkit.entity.Player;

public class ParametersInv implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> ConfigurationInv.INVENTORY.open((Player) ev.getWhoClicked())));

        contents.set(0, 0, ClickableItem.of(new ParametersGlobalItemStack(), ev -> ParametersGlobalInv.INVENTORY.open(((Player)ev.getWhoClicked()))));
        contents.set(0, 1, ClickableItem.of(new ParametersRolesItemStack(), ev -> ParametersRolesInv.INVENTORY.open(((Player)ev.getWhoClicked()))));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("config_all")
            .provider(new ParametersInv())
            .size(1, 9)
            .title("§f§lParamètres de la partie")
            .closeable(true)
            .build();

}
