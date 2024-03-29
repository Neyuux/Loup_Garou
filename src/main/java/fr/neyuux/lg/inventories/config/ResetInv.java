package fr.neyuux.lg.inventories.config;

import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.reset.NoResetMapItemStack;
import fr.neyuux.lg.items.menus.config.reset.YesResetMapItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ResetInv extends AbstractCustomInventory {
    public ResetInv() {
        super("�b�lReset �bla Map", 27);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)3);
        this.setItem(26, new ReturnArrowItemStack(new ConfigurationInv()));

        this.setItem(11, new YesResetMapItemStack());
        this.setItem(15, new NoResetMapItemStack());
    }
}
