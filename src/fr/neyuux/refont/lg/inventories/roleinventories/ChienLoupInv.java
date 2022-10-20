package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.reset.NoResetMapItemStack;
import fr.neyuux.refont.lg.items.menus.config.reset.YesResetMapItemStack;
import fr.neyuux.refont.lg.roles.classes.ChienLoup;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ChienLoupInv extends AbstractCustomInventory {
    public ChienLoupInv(ChienLoup chienloup) {
        super(chienloup.getDisplayName(), 27);
    }

    @Override
    public void registerItems() {
        this.setCorner((byte)5, 0, (byte)1);
        this.setCorner((byte)5, this.getSize() - 9, (byte)3);
        this.setCorner((byte)14, 8, (byte)2);
        this.setCorner((byte)14, this.getSize() - 1, (byte)4);

        this.setItem(11, new YesResetMapItemStack());
        this.setItem(15, new NoResetMapItemStack());
    }
}
