package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.items.menus.roleinventories.ChienLoupDogChoiceItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ChienLoupWolfChoiceItemStack;
import fr.neyuux.lg.roles.classes.ChienLoup;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ChienLoupInv extends AbstractCustomInventory {

    private final ChienLoup chienLoup;
    private final Runnable callback;

    public ChienLoupInv(ChienLoup chienloup, Runnable callback) {
        super(chienloup.getDisplayName(), 27);

        this.callback = callback;
        this.chienLoup = chienloup;
    }

    @Override
    public void registerItems() {
        this.setCorner((byte)5, 0, (byte)1);
        this.setCorner((byte)5, this.getSize() - 9, (byte)3);
        this.setCorner((byte)14, 8, (byte)2);
        this.setCorner((byte)14, this.getSize() - 1, (byte)4);

        this.setItem(11, new ChienLoupDogChoiceItemStack(callback));
        this.setItem(15, new ChienLoupWolfChoiceItemStack(callback));
    }
}
