package fr.neyuux.lg.inventories.config.parameters;


import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.*;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ParametersRolesInv extends AbstractCustomInventory {
    public ParametersRolesInv() {
        super("§f§lParamètres §6§ldes Rôles", 36);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)13);
        this.setItem(35, new ReturnArrowItemStack(new ParametersInv()));

        //items 11 -> 15 / 20 -> 24
        this.setItem(11, new ParameterCupiWinWithCoupleItemStack());
        this.setItem(12, new ParameterComedianPowersItemStack());
        this.setItem(13, new ParameterWildChildRandomModelItemStack());
        this.setItem(14, new ParameterCupiInCoupleItemStack());
        this.setItem(15, new ParameterChamanChatItemStack());
        this.setItem(22, new ParameterChattyVoyanteItemStack());
    }
}
