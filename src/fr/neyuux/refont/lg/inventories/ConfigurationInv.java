package fr.neyuux.refont.lg.inventories;

import fr.neyuux.refont.lg.items.config.mainmenu.OpListItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ConfigurationInv extends AbstractCustomInventory {

    public ConfigurationInv() {
        super("§c§lConfiguration", 45);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(38, new OpListItemStack());

    }

}
