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

public class ParameterRandomCoupleItemStack extends CustomItemStack {

    private final Parameter randomCoupleParamter;

    public ParameterRandomCoupleItemStack() {
        super(Material.BOW, 1, "§dCouple §7Random");
        this.randomCoupleParamter = LG.getInstance().getGame().getConfig().getRandomCouple();

        this.setLore("§fActive ou non la sélection", "§faléatoire du couple.", "", "§bValeur : " + randomCoupleParamter.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        randomCoupleParamter.setValue(!(boolean)randomCoupleParamter.getValue());
        this.setLoreLine(3, "§bValeur : " + randomCoupleParamter.getVisibleValue());
        inv.setItem(slot, this);
    }
}
