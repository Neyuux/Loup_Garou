package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.Parameter;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ParameterWildChildRandomModelItemStack extends CustomItemStack {

    private final Parameter randomModel;

    public ParameterWildChildRandomModelItemStack() {
        super(Material.WEB, 1, "§eModèle de l'§6§lEnfant Sauvage §eRandom");
        this.randomModel = LG.getInstance().getGame().getConfig().getWildChildRandomModel();

        this.setLore("§fActive ou non la génération", "§faléatoire du modèle de l'enfant sauvage.", "", "§bValeur : " + randomModel.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        randomModel.setValue(!(boolean)randomModel.getValue());
        this.setLoreLine(3, "§bValeur : " + randomModel.getVisibleValue());

        inv.setItem(slot, this);
    }
}
