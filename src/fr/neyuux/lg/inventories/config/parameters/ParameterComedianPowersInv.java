package fr.neyuux.lg.inventories.config.parameters;


import fr.neyuux.lg.config.ComedianPowers;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.parameters.ComedianPowerGlassItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

public class ParameterComedianPowersInv extends AbstractCustomInventory {
    public ParameterComedianPowersInv() {
        super("§dRôles du §5§lComédien", 54);
        this.adaptIntToInvSize(ComedianPowers.values().length + 1);
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new ReturnArrowItemStack(new ParametersRolesInv()));

        for (ComedianPowers power : ComedianPowers.values())
            this.addItem(new ComedianPowerGlassItemStack(power));
    }
}
