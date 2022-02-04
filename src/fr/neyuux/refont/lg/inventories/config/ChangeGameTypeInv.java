package fr.neyuux.refont.lg.inventories.config;

import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ChangeGameTypeInv extends AbstractCustomInventory {
    public ChangeGameTypeInv() {
        super("§2§lType §cde la Partie", 27);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)13);


    }
}
