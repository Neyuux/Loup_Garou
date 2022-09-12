package fr.neyuux.refont.lg.inventories.config.roles;

import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.roles.DecksItemStack;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class RoleDecksInv extends AbstractCustomInventory {
    public RoleDecksInv() {
        super("§6§lMenu §bDecks", 9);
    }

    @Override
    public void registerItems() {
        this.setItem(8, new ReturnArrowItemStack(new ConfigurationInv()));

        for (Decks deck : Decks.values())
            this.addItem(new DecksItemStack(deck));
    }
}
