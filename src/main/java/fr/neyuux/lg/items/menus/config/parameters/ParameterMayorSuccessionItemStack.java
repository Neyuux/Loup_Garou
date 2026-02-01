package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.MayorSuccession;
import fr.neyuux.lg.config.Parameter;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ParameterMayorSuccessionItemStack extends CustomItemStack {

    private final Parameter mayorSuccessionParameter;

    public ParameterMayorSuccessionItemStack() {
        super(Material.ARMOR_STAND, 1, "§9Succession du maire");
        this.mayorSuccessionParameter = LG.getInstance().getGame().getConfig().getMayorSuccession();

        this.setLore("§fChange le type de succession du maire", "", "§bValeur : " + mayorSuccessionParameter.getVisibleValue(), ((MayorSuccession)mayorSuccessionParameter.getValue()).getDescription(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        mayorSuccessionParameter.setValue(MayorSuccession.getNext(((MayorSuccession)mayorSuccessionParameter.getValue()).getId()));
        this.setLoreLine(2, "§bValeur : " + mayorSuccessionParameter.getVisibleValue());
        this.setLoreLine(3, ((MayorSuccession)mayorSuccessionParameter.getValue()).getDescription());

        inv.setItem(slot, this);
    }
}
