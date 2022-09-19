package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;

public class ParameterChattyVoyanteItemStack extends CustomItemStack {

    private final Parameter chattyVoyante;

    public ParameterChattyVoyanteItemStack() {
        super(Material.EYE_OF_ENDER, 1, "§d§lVoyante §bbavarde");
        this.chattyVoyante = LG.getInstance().getGame().getConfig().getChattyVoyante();

        this.setLore("§fActive ou non le message dans le", "§fchat le rôle de la personne qui a été vue.", "", "§bValeur : " + chattyVoyante.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        chattyVoyante.setValue(!(boolean)chattyVoyante.getValue());
        this.setLoreLine(3, "§bValeur : " + chattyVoyante.getVisibleValue());
        this.updateInInv(((InventoryEvent)event).getInventory());
    }
}
