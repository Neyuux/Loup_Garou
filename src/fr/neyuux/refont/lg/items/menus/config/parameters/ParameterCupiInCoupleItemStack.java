package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;

public class ParameterCupiInCoupleItemStack extends CustomItemStack {

    private final Parameter cupiInCouple;

    public ParameterCupiInCoupleItemStack() {
        super(Material.HOPPER, 1, "§9§lCupi§d§ldon §den couple");
        this.cupiInCouple = LG.getInstance().getGame().getConfig().getCupiInCouple();

        this.setLore("§fActive ou non l'obligation", "§fdu Cupidon à faire partie du couple.", "", "§bValeur : " + cupiInCouple.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        cupiInCouple.setValue(!(boolean)cupiInCouple.getValue());
        this.setLoreLine(3, "§bValeur : " + cupiInCouple.getVisibleValue());
        this.updateInInv(((InventoryEvent)event).getInventory());
    }
}
