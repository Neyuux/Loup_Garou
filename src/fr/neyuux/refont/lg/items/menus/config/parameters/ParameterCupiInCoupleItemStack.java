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

public class ParameterCupiInCoupleItemStack extends CustomItemStack {

    private final Parameter cupiInCouple;

    public ParameterCupiInCoupleItemStack() {
        super(Material.HOPPER, 1, "§9§lCupi§d§ldon §den couple");
        this.cupiInCouple = LG.getInstance().getGame().getConfig().getCupiInCouple();

        this.setLore("§fActive ou non l'obligation", "§fdu Cupidon à faire partie du couple.", "", "§bValeur : " + cupiInCouple.getVisibleValue(), "", "§7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        cupiInCouple.setValue(!(boolean)cupiInCouple.getValue());
        this.setLoreLine(3, "§bValeur : " + cupiInCouple.getVisibleValue());
        inv.setItem(slot, this);
    }
}
