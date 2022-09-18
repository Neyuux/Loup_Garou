package fr.neyuux.refont.lg.inventories.config.parameters;


import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.ComedianPowers;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.parameters.*;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class ParameterComedianPowersInv extends AbstractCustomInventory {
    public ParameterComedianPowersInv() {
        super("§dRôles du §5§lComédien", 54, 11);
        this.adaptIntToInvSize(ComedianPowers.values().length + 1);
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new ReturnArrowItemStack(new ParametersRolesInv()));

        for (ComedianPowers power : ComedianPowers.values())
            this.addItem(new ComedianPowerGlassItemStack(power));
    }
}
