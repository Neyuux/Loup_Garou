package fr.neyuux.refont.lg.inventories.config.roles;

import fr.neyuux.refont.lg.inventories.config.parameters.ParametersRolesInv;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.roles.CampChooseItemStack;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class RolesCampChooseInv extends AbstractCustomInventory {

    private final Decks deck;

    public RolesCampChooseInv(Decks deck) {
        super("§6§lMenu §aCamps", 9, 6);
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
