package fr.neyuux.lg.inventories.config.roles;

import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.roles.CampChooseItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class RolesCampChooseInv extends AbstractCustomInventory {

    private final Decks deck;

    public RolesCampChooseInv(Decks deck) {
        super("§6§lMenu §aCamps", 9);
        this.deck = deck;
    }

    @Override
    public void registerItems() {
        this.setItem(8, new ReturnArrowItemStack(new RoleDecksInv()));

        this.setItem(0, new CampChooseItemStack(deck, Camps.LOUP_GAROU));
        this.setItem(3, new CampChooseItemStack(deck, Camps.VILLAGE));
        this.setItem(6, new CampChooseItemStack(deck, Camps.AUTRE));

    }
}
