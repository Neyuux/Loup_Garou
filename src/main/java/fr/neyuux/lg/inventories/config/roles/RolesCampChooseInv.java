package fr.neyuux.lg.inventories.config.roles;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.roles.CampChooseItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import org.bukkit.entity.Player;

public class RolesCampChooseInv implements InventoryProvider {

    private final Decks deck;

    public RolesCampChooseInv(Decks deck) {
        this.deck = deck;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> RoleDecksInv.INVENTORY.open((Player) ev.getWhoClicked())));

        CampChooseItemStack lg = new CampChooseItemStack(deck, Camps.LOUP_GAROU);
        contents.set(0, 0, ClickableItem.of(lg, ev -> lg.use(ev.getWhoClicked(), ev)));

        CampChooseItemStack village = new CampChooseItemStack(deck, Camps.VILLAGE);
        contents.set(0, 3, ClickableItem.of(village, ev -> village.use(ev.getWhoClicked(), ev)));

        CampChooseItemStack other = new CampChooseItemStack(deck, Camps.AUTRE);
        contents.set(0, 6, ClickableItem.of(other, ev -> other.use(ev.getWhoClicked(), ev)));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }


    public static SmartInventory getInventory(Decks deck) {
        return SmartInventory.builder()
                .id("roles_camp")
                .provider(new RolesCampChooseInv(deck))
                .size(1, 9)
                .title("§6§lMenu §aCamps")
                .closeable(true)
                .build();
    }
}
