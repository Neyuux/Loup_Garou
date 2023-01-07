package fr.neyuux.lg.inventories.config;

import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.changegametypeinv.ChangeGameTypeToFreeItemStack;
import fr.neyuux.lg.items.menus.config.changegametypeinv.ChangeGameTypeToMeetingItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ChangeGameTypeInv extends AbstractCustomInventory {
    public ChangeGameTypeInv() {
        super("§2§lType §cde la Partie", 27);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)13);
        this.setItem(26, new ReturnArrowItemStack(new ConfigurationInv()));

        this.setItem(11, new ChangeGameTypeToFreeItemStack());
        this.setItem(15, new ChangeGameTypeToMeetingItemStack());
    }
}
