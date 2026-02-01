package fr.neyuux.lg.inventories.config.parameters;


import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.ComedianPowers;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ComedianPowerGlassItemStack;
import org.bukkit.entity.Player;

public class ParameterComedianPowersInv implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(ComedianPowers.values().length, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> ParametersRolesInv.INVENTORY.open((Player) ev.getWhoClicked())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        for (int i = 0; i < ComedianPowers.values().length; i++) {
            ComedianPowers power = ComedianPowers.values()[i];
            ComedianPowerGlassItemStack item = new ComedianPowerGlassItemStack(power);
            contents.set(i / 9, i % 9, ClickableItem.of(item, ev -> item.use(ev.getWhoClicked(), ev)));
        }
    }


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("config_comedian")
            .provider(new ParameterComedianPowersInv())
            .size(LG.adaptIntToInvSize(ComedianPowers.values().length + 1), 9)
            .title("§dRôles du §5§lComédien")
            .closeable(true)
            .build();
}
