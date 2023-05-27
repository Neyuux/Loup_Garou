package fr.neyuux.lg.inventories.config.parameters;


import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.*;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ParametersGlobalInv extends AbstractCustomInventory {
    public ParametersGlobalInv() {
        super("§f§lParamètres §a§lGlobaux", 27);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)5);
        this.setItem(26, new ReturnArrowItemStack(new ParametersInv()));

        //items 11 -> 15 / 20 -> 24
        this.setItem(11, new ParameterDayCycleItemStack());
        this.setItem(12, new ParameterLGChatItemStack());
        this.setItem(13, new ParameterMayorItemStack());
        this.setItem(14, new ParameterRandomCoupleItemStack());
        this.setItem(15, new ParameterMayorSuccessionItemStack());
    }
}
