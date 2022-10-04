package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class ParameterMayorItemStack extends CustomItemStack {

    private final Parameter mayorParameter;

    public ParameterMayorItemStack() {
        super(Material.SKULL_ITEM, 1, "§bMaire");
        this.mayorParameter = LG.getInstance().getGame().getConfig().getMayor();

        this.setLore("§Gère le fait que le", "§fmaire soit activé ou non.", "", "§bValeur : §l" + mayorParameter.getVisibleValue(), "", "§7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        mayorParameter.setValue(!(boolean)mayorParameter.getValue());
        this.setLoreLine(3, "§bValeur : §l" + mayorParameter.getVisibleValue());
        inv.setItem(slot, this);
    }
}
