package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.Parameter;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ParameterLGChatItemStack extends CustomItemStack {

    private final Parameter chatLGParameter;

    public ParameterLGChatItemStack() {
        super(Material.SIGN, 1, "§cChat des Loups");
        this.chatLGParameter = LG.getInstance().getGame().getConfig().getChatLG();

        this.setLore("§fActive ou non le chat", "§fentre loups-garous la nuit.", "", "§bValeur : " + chatLGParameter.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        chatLGParameter.setValue(!(boolean)chatLGParameter.getValue());
        this.setLoreLine(3, "§bValeur : " + chatLGParameter.getVisibleValue());
        inv.setItem(slot, this);
    }
}
