package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.Parameter;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ParameterChattyVoyanteItemStack extends CustomItemStack {

    private final Parameter chattyVoyante;

    public ParameterChattyVoyanteItemStack() {
        super(Material.EYE_OF_ENDER, 1, "§d§lVoyante §bbavarde");
        this.chattyVoyante = LG.getInstance().getGame().getConfig().getChattyVoyante();

        this.setLore("§fActive ou non le message dans le", "§fchat le rôle de la personne qui a été vue.", "", "§bValeur : " + chattyVoyante.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        chattyVoyante.setValue(!(boolean)chattyVoyante.getValue());
        this.setLoreLine(3, "§bValeur : " + chattyVoyante.getVisibleValue());
        inv.setItem(slot, this);
    }
}
