package fr.neyuux.lg.inventories.config.parameters;

import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ParametersGlobalItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ParametersRolesItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ParametersInv extends AbstractCustomInventory {
    public ParametersInv() {
        super("§f§lParamètres de la partie", 9);
    }

    @Override
    public void registerItems() {
        this.setItem(8, new ReturnArrowItemStack(new ConfigurationInv()));

        this.setItem(0, new ParametersGlobalItemStack());
        this.setItem(1, new ParametersRolesItemStack());
    }
}
