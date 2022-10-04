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

public class ParameterWildChildRandomModelItemStack extends CustomItemStack {

    private final Parameter randomModel;

    public ParameterWildChildRandomModelItemStack() {
        super(Material.WEB, 1, "�eMod�le de l'�6�lEnfant Sauvage �eRandom");
        this.randomModel = LG.getInstance().getGame().getConfig().getWildChildRandomModel();

        this.setLore("�fActive ou non la g�n�ration", "�fal�atoire du mod�le de l'enfant sauvage.", "", "�bValeur : " + randomModel.getVisibleValue(), "", "�7>>Clique pour modifier");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        randomModel.setValue(!(boolean)randomModel.getValue());
        this.setLoreLine(3, "�bValeur : " + randomModel.getVisibleValue());

        inv.setItem(slot, this);
    }
}
