package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.inventories.config.parameters.ParametersGlobalInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class ParameterDayCycleItemStack extends CustomItemStack {

    private final Parameter dayCycleParameter;

    public ParameterDayCycleItemStack() {
        super(Material.WATCH, 1, "§6Modification du Cycle §eJour§6/§9Nuit");
        this.dayCycleParameter = LG.getInstance().getGame().getConfig().getDayCycle();

        this.setLore("§fActive ou non le changement", "§fd'atmosphère selon le cycle jour/nuit", "§f§o(Faisant passer du jour à la nuit)", "", "§bValeur : " + dayCycleParameter.getVisibleValue(), "", "§7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        dayCycleParameter.setValue(!(boolean)dayCycleParameter.getValue());
        this.setLoreLine(4, "§bValeur : " + dayCycleParameter.getVisibleValue());
        inv.setItem(slot, this);
    }
}
