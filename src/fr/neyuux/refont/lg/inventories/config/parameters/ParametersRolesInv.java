package fr.neyuux.refont.lg.inventories.config.parameters;


import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.parameters.*;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ParametersRolesInv extends AbstractCustomInventory {
    public ParametersRolesInv() {
        super("�f�lParam�tres �6�ldes R�les", 36, 8);
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
