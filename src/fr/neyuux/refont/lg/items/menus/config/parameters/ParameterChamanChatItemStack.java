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

public class ParameterChamanChatItemStack extends CustomItemStack {

    private final Parameter chamanChat;

    public ParameterChamanChatItemStack() {
        super(Material.PAPER, 1, "§fParole du §b§lCha§a§lman");
        this.chamanChat = LG.getInstance().getGame().getConfig().getChamanChat();

        this.setLore("§fSi cette option est activée, le", "§fChaman pourra envoyer des message aux morts",  "§f(en plus de les recevoir).", "§fSinon il pourra juste recevoir les messages des morts.", "", "§bValeur : §l" + chamanChat.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        chamanChat.setValue(!(boolean)chamanChat.getValue());
        this.setLoreLine(5, "§bValeur : §l" + chamanChat.getVisibleValue());
        inv.setItem(slot, this);
    }
}
