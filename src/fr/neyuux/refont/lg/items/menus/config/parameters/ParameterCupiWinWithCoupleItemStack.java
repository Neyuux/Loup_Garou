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

public class ParameterCupiWinWithCoupleItemStack extends CustomItemStack {

    private final Parameter cupiWinWithCouple;

    public ParameterCupiWinWithCoupleItemStack() {
        super(Material.TRIPWIRE_HOOK, 1, "§9§lCupi§d§lDon §fgagne avec son couple");
        this.cupiWinWithCouple = LG.getInstance().getGame().getConfig().getCupiWinWithCouple();

        this.setLore("§fActive ou non le fait que", "§fCupidon peut gagner avec son couple", "", "§bValeur : " + cupiWinWithCouple.getVisibleValue(), "", "§7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        cupiWinWithCouple.setValue(!(boolean)cupiWinWithCouple.getValue());
        this.setLoreLine(3, "§bValeur : " + cupiWinWithCouple.getVisibleValue());
        inv.setItem(slot, this);
    }
}
