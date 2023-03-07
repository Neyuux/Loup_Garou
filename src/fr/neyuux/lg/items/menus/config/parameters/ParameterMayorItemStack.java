package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.Parameter;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ParameterMayorItemStack extends CustomItemStack {

    private final Parameter mayorParameter;

    public ParameterMayorItemStack() {
        super(Material.SKULL_ITEM, 1, "�bMaire");
        this.mayorParameter = LG.getInstance().getGame().getConfig().getMayor();

        this.setLore("�fGG�re le fait que le", "�fmaire soit activ� ou non.", "", "�bValeur : �l" + mayorParameter.getVisibleValue(), "", "�7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        mayorParameter.setValue(!(boolean)mayorParameter.getValue());
        this.setLoreLine(3, "�bValeur : �l" + mayorParameter.getVisibleValue());
        inv.setItem(slot, this);
    }
}
