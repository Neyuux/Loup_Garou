package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.inventories.config.parameters.ParameterComedianPowersInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParameterComedianPowersItemStack extends CustomItemStack {

    public ParameterComedianPowersItemStack() {
        super(Material.PUMPKIN, 1, "§dPouvoirs du §5§lComédien");

        this.setLore("§fGère les rôles que", "§fle Comédien peut imiter.", "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ParameterComedianPowersInv().open(player);
    }
}
