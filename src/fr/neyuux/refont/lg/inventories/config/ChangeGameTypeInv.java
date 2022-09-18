package fr.neyuux.refont.lg.inventories.config;

import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.changegametypeinv.ChangeGameTypeToFreeItemStack;
import fr.neyuux.refont.lg.items.menus.config.changegametypeinv.ChangeGameTypeToMeetingItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ChangeGameTypeInv extends AbstractCustomInventory {
    public ChangeGameTypeInv() {
        super("§2§lType §cde la Partie", 27, 1);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)13);
        this.setItem(26, new ReturnArrowItemStack(new ConfigurationInv()));

        this.setItem(11, new ChangeGameTypeToFreeItemStack());
        this.setItem(15, new ChangeGameTypeToMeetingItemStack());
    }
}
